package gachagacha.db.outbox;

import gachagacha.domain.outbox.Outbox;
import gachagacha.domain.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEntityRepository implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public List<Outbox> findAllByTopic(String topic) {
        return outboxJpaRepository.findAllByTopic(topic).stream()
                .map(outboxEntity -> outboxEntity.toOutbox())
                .toList();
    }

    @Override
    public void delete(Outbox outbox) {
        outboxJpaRepository.delete(OutboxEntity.fromOutbox(outbox));
    }

    @Override
    public Long save(Outbox outbox) {
        OutboxEntity outboxEntity = outboxJpaRepository.save(OutboxEntity.fromOutbox(outbox));
        return outboxEntity.getId();
    }
}
