package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.lotto.dto.LotteryIssuanceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoMessagePublisher {

    private final RedisTemplate<String, LotteryIssuanceEvent> redisTemplate;

    public void publishLotteryIssuanceEvent(LotteryIssuanceEvent message) {
        log.info("Redis publish. topic = {}, message = {}", "lotto:issuance:event", message.toString());
        redisTemplate.convertAndSend("lotto:issuance:event", message);
    }
}
