package gachagacha.gachagacha.domain.notification;

import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationProcessor notificationProcessor;

    public List<Notification> readNotifications(User user) {
        return notificationProcessor.readNotifications(user);
    }

    public Optional<Long> getLastReadNotificationId(User user) {
        return notificationProcessor.getLastReadNotificationId(user);
    }

    public void markLastReadNotification(long lastReadNotificationId, User user) {
        notificationProcessor.markLastReadNotification(lastReadNotificationId, user);
    }
}
