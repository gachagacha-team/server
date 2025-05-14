package gachagacha.db.notification;

import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationEntityRepository implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationReadMarkerJpaRepository notificationReadMarkerJpaRepository;

    @Override
    public List<Notification> readRecentNotifications(User user) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return notificationJpaRepository.findByUserIdAndCreatedAtAfter(user.getId(), sevenDaysAgo).stream()
                .map(notificationEntity -> notificationEntity.toNotification())
                .toList();
    }

    @Override
    public Long save(Notification notification) {
        NotificationEntity notificationEntity = notificationJpaRepository.save(NotificationEntity.fromNotification(notification));
        return notificationEntity.getId();
    }

    @Override
    public boolean hasNewNotification(Long userId) {
        Optional<NotificationReadMarkerEntity> optionalNotificationReadMarker = notificationReadMarkerJpaRepository.findByUserId(userId);
        Optional<NotificationEntity> optionalNotification = notificationJpaRepository.findTopByUserIdOrderByCreatedAtDesc(userId);

        if (optionalNotification.isEmpty()) {
            return false;
        }
        if (optionalNotificationReadMarker.isEmpty()) {
            return true;
        }
        return optionalNotification.get().getId() > optionalNotificationReadMarker.get().getId();
    }

    @Override
    public void markLastReadNotification(User user) {
        Optional<NotificationEntity> optionalNotification = notificationJpaRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());
        if (optionalNotification.isPresent())  {
            NotificationEntity notificationEntity = optionalNotification.get();
            notificationReadMarkerJpaRepository.deleteByUserId(user.getId());
            notificationReadMarkerJpaRepository.save(NotificationReadMarkerEntity.of(user.getId(), notificationEntity.getId()));
        }
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return notificationJpaRepository.findById(notificationId)
                .map(notificationEntity -> notificationEntity.toNotification());
    }
}
