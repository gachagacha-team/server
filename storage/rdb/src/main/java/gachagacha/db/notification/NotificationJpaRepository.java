package gachagacha.db.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime after);

    Optional<NotificationEntity> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
