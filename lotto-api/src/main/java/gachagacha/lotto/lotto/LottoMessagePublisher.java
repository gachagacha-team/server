package gachagacha.lotto.lotto;

import gachagacha.domain.lotto.IssuedLotto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoMessagePublisher {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.data.redis.stream.lotto-issued}")
    private String streamKey;

    public void publishIssuedLotto(IssuedLotto issuedLotto) {
        ObjectRecord<String, IssuedLotto> record = StreamRecords.newRecord()
                .ofObject(issuedLotto)
                .withStreamKey(streamKey);
        RecordId recordId = redisTemplate.opsForStream().add(record);
        log.info("Redis Stream publish. stream = {}, record id = {}, message = {}", streamKey, recordId, issuedLotto.toString());
    }
}
