package gachagacha.domain.notification;

import gachagacha.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository {

    List<Notification> readRecentNotifications(User user);

    Long saveNotification(Notification notification);

    boolean hasNewNotification(Long userId);

    void markLastReadNotification(User user);

    Optional<Notification> findById(Long notificationId);
}
