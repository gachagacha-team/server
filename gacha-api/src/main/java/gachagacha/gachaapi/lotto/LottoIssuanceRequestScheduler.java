package gachagacha.gachaapi.lotto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.domain.lotto.LottoIssuanceEvent;
import gachagacha.domain.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoIssuanceRequestScheduler {

    private final OutboxRepository outboxRepository;
    private final LottoMessagePublisher lottoMessagePublisher;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.stream.lotto-issuance-requests}")
    private String topic;

    @Scheduled(fixedRate = 5000)
    public void publishOutboxMessage()  {
        outboxRepository.findAllByTopic(topic).stream()
                .forEach(outbox -> {
                    LottoIssuanceEvent lottoIssuanceEvent = null;
                    try {
                        lottoIssuanceEvent = objectMapper.readValue(outbox.getPayload(), LottoIssuanceEvent.class);
                    } catch (JsonProcessingException e) {
                        //todo
                        throw new RuntimeException(e);
                    }
                    lottoMessagePublisher.publishLottoIssuanceEvent(lottoIssuanceEvent);
                    outboxRepository.delete(outbox);
                });
    }
}
