package gachagacha.db.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {

    List<OutboxEntity> findAllByTopic(String topic);
}
