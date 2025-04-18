package gachagacha.gachagacha.domain.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.gachagacha.domain.lotto.LottoMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final LottoMessagePublisher lottoMessagePublisher;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.stream.lotto-issuance-requests}")
    private String topic;

    @Scheduled(fixedRate = 5000)
    public void publishOutboxMessage() throws JsonProcessingException {
        List<Outbox> outboxes = outboxRepository.findAll();
        for (Outbox outbox : outboxes) {
            if (outbox.getTopic().equals(topic)) {
                LottoIssuanceEvent lottoIssuanceEvent = objectMapper.readValue(outbox.getPayload(), LottoIssuanceEvent.class);
                lottoMessagePublisher.publishLottoIssuanceEvent(lottoIssuanceEvent);
                outboxRepository.delete(outbox);
            }
        }
    }

    public void save(Outbox outbox) {
        outboxRepository.save(outbox);
    }
}
