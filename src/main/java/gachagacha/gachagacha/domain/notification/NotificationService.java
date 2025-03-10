package gachagacha.gachagacha.domain.notification;

import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationProcessor notificationProcessor;

    public List<Notification> readNotifications(User user) {
        return notificationProcessor.readNotifications(user);
    }

    public boolean isRead(long notificationId) {
        return notificationProcessor.isRead(notificationId);
    }

    public void readOneNotification(long notificationId, User user) {
        notificationProcessor.readOneNotification(notificationId);
    }
}
