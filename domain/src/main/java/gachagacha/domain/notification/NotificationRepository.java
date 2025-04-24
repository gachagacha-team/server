package gachagacha.domain.notification;

import gachagacha.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository {

    List<Notification> readNotifications(User user);

    Long saveNotification(Notification notification, User user);

    Optional<Long> getLastReadNotificationId(User user);

    void markLastReadNotification(long lastReadNotificationId, User user);
}
