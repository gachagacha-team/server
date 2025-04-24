package gachagacha.lotto.lotto;

import gachagacha.domain.item.ItemGrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoMessageListener implements StreamListener<String, MapRecord<String, String, String>>, InitializingBean {

    @Value("${spring.data.redis.stream.lotto-issuance-requests}")
    private String streamKey;
    private final String CONSUMER_GROUP_NAME = "default-group";
    private final String CONSUMER_NAME = "default-name";
    private final StreamMessageListenerContainer listenerContainer;
    private final RedisTemplate<String, String> redisTemplate;
    private final LottoIssuanceService lottoIssuanceService;

    @Value("${spring.data.redis.stream.lotto-issued}")
    private String topic;
    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.info("Redis Stream consume. stream = {}, message = {}", streamKey, message.toString());
        try {
            Long userId = Long.valueOf(message.getValue().get("userId"));
            ItemGrade itemGrade = ItemGrade.findByViewName(message.getValue().get("itemGrade"));
            if (lottoIssuanceService.checkLottoIssuanceCondition(userId, itemGrade)) {
                lottoIssuanceService.issueAndSaveLotto(userId, itemGrade, topic);
            }

            redisTemplate.opsForStream().acknowledge(streamKey, CONSUMER_GROUP_NAME, message.getId());
        } catch (Exception e) {
            // 중복된 복권 INSERT되면 유니크 제약 조건으로 인해 예외 발생하여 트랜잭션 롤백된 후 해당 구간이 실행됨
            log.error("메시지 처리 중 예외 발생");
        }
    }

//    @Scheduled(fixedRate = 30000)
//    public void pendingMessageScheduler() {
//
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(streamKey))) {
//            log.info("Stream key '{}' does not exist. Skipping pending message processing.", streamKey);
//            return;
//        }
//
//        StreamOperations<String, String, String> streamOps = redisTemplate.opsForStream();
//        PendingMessagesSummary summary = streamOps.pending(streamKey, CONSUMER_GROUP_NAME);
//        long totalPendingMessagesCount = summary.getTotalPendingMessages();
//        if (totalPendingMessagesCount > 0) {
//            log.info("Start pending message scheduler");
//            PendingMessages pendingMessages = streamOps.pending(streamKey, CONSUMER_GROUP_NAME, Range.closed("0", "+"), totalPendingMessagesCount);
//            RecordId[] pendingMessageIds = pendingMessages.stream()
//                    .map(pendingMessage -> pendingMessage.getId())
//                    .toArray(RecordId[]::new);
//            streamOps.claim(streamKey, CONSUMER_GROUP_NAME, CONSUMER_NAME, Duration.ofMillis(0), pendingMessageIds)
//                    .stream()
//                    .forEach(message -> {
//                        log.info("Retry pending message = {}", message.getValue());
//                        process(message);
//                    });
//        }
//    }

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
