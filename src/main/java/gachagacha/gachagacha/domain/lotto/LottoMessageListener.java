package gachagacha.gachagacha.domain.lotto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.gachagacha.api.sse.SseEmitters;
import gachagacha.gachagacha.domain.lotto.dto.IssuedLotto;
import gachagacha.gachagacha.domain.lotto.dto.LotteryIssuanceEvent;
import gachagacha.gachagacha.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoMessageListener implements MessageListener {

    private final RedisTemplate<String, LotteryIssuanceEvent> redisTemplate;
    private final ObjectMapper objectMapper;
    private final LottoProcessor lottoProcessor;
    private final UserReader userReader;
    private final SseEmitters sseEmitters;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate
                    .getStringSerializer().deserialize(message.getBody());
            IssuedLotto issuedLotto = objectMapper.readValue(publishMessage, IssuedLotto.class);
            log.info("Redis subscribe. message = {}", issuedLotto.toString());

            // 로또 저장
            Lotto savedLotto = lottoProcessor.save(Lotto.of(issuedLotto.getUserId(), issuedLotto.isWon(), issuedLotto.getRewardCoin()));

            // SSE로 알리기
            sseEmitters.issuedLotto(savedLotto, userReader.findById(savedLotto.getUserId()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
