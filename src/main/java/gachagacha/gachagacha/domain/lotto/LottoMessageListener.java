package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.api.sse.SseEmitters;
import gachagacha.gachagacha.domain.item.ItemGrade;
import gachagacha.gachagacha.domain.notification.Notification;
import gachagacha.gachagacha.domain.notification.NotificationProcessor;
import gachagacha.gachagacha.domain.notification.NotificationType;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoMessageListener implements StreamListener<String, MapRecord<String, String, String>>, InitializingBean {

    private final LottoProcessor lottoProcessor;
    private final StreamMessageListenerContainer listenerContainer;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserReader userReader;
    private final SseEmitters sseEmitters;
    private final NotificationProcessor notificationProcessor;

    @Value("${spring.data.redis.stream.lotto-issued}")
    private String streamKey;
    private final String CONSUMER_GROUP_NAME = "default-group";
    private final String CONSUMER_NAME = "default-name";

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.info("Redis Stream consume. stream = {}, message = {}", streamKey, message.toString());
        try {
            process(message);
        } catch (Exception e) {
            log.error("메시지 처리 중 예외 발생");
        }
    }

    private void process(MapRecord<String, String, String> message) {
        Map<String, String> map = message.getValue();


        Long userId = Long.valueOf(message.getValue().get("userId"));
        Long lottoId = Long.valueOf(message.getValue().get("lottoId"));

        Lotto lotto = lottoProcessor.findById(lottoId);
        User user = userReader.findById(userId);
        ItemGrade itemGrade = lotto.getItemGrade();

        Notification notification = Notification.of(
                NotificationType.LOTTO_ISSUED,
                new Notification.LottoIssuedNotification(lottoId, itemGrade.getViewName()));
        Long notificationId = notificationProcessor.saveNotification(notification, user);

        sseEmitters.issuedLotto(notification, user);

        redisTemplate.opsForStream().acknowledge(streamKey, CONSUMER_GROUP_NAME, message.getId());
    }

//    @Scheduled(fixedRate = 100000)
    public void pendingMessageScheduler() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(streamKey))) {
            log.info("Stream key '{}' does not exist. Skipping pending message processing.", streamKey);
            return;
        }

        StreamOperations<String, String, String> streamOps = redisTemplate.opsForStream();
        PendingMessagesSummary summary = streamOps.pending(streamKey, CONSUMER_GROUP_NAME);
        long totalPendingMessagesCount = summary.getTotalPendingMessages();
        if (totalPendingMessagesCount > 0) {
            log.info("Start pending message scheduler");
            PendingMessages pendingMessages = streamOps.pending(streamKey, CONSUMER_GROUP_NAME, Range.closed("0", "+"), totalPendingMessagesCount);
            RecordId[] pendingMessageIds = pendingMessages.stream()
                    .map(pendingMessage -> pendingMessage.getId())
                    .toArray(RecordId[]::new);
            streamOps.claim(streamKey, CONSUMER_GROUP_NAME, CONSUMER_NAME, Duration.ofMillis(0), pendingMessageIds)
                    .stream()
                    .forEach(message -> {
                        log.info("Retry pending message = {}", message.getValue());
                        process(message);
                    });
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(streamKey))) {
            redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.from("0"), CONSUMER_GROUP_NAME);
        } else {
            boolean groupExists = redisTemplate.opsForStream()
                    .groups(streamKey)
                    .stream()
                    .anyMatch(group -> group.groupName().equals(CONSUMER_GROUP_NAME));
            if (!groupExists) {
                redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.latest(), CONSUMER_GROUP_NAME);
            }
        }

        Consumer consumer = Consumer.from(CONSUMER_GROUP_NAME, CONSUMER_NAME);
        StreamOffset<String> streamOffset = StreamOffset.create(streamKey, ReadOffset.lastConsumed());
        listenerContainer.receive(consumer, streamOffset, this);
        listenerContainer.start();
    }
}
