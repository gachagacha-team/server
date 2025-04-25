package gachagacha.domain.outbox;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository {
    
    List<Outbox> findAllByTopic(String topic);

    void delete(Outbox outbox);

    Long save(Outbox outbox);
}
