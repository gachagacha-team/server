package gachagacha.lotto.lotto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.domain.lotto.IssuedLotto;
import gachagacha.domain.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssuedLottoMessageScheduler {

    private final OutboxRepository outboxRepository;
    private final LottoMessagePublisher lottoMessagePublisher;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.stream.lotto-issued}")
    private String topic;

    @Scheduled(fixedRate = 5000)
    public void publishOutboxMessage() {
        outboxRepository.findAllByTopic(topic).stream()
                .forEach(outbox -> {
                    IssuedLotto issuedLotto = null;
                    try {
                        issuedLotto = objectMapper.readValue(outbox.getPayload(), IssuedLotto.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    lottoMessagePublisher.publishIssuedLotto(issuedLotto);
                    outboxRepository.delete(outbox);
                });
    }
}
