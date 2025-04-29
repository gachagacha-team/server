package gachagacha.db.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationReadMarkerJpaRepository extends JpaRepository<NotificationReadMarkerEntity, Long> {

    Optional<NotificationReadMarkerEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
