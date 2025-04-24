package gachagacha.gachaapi.service;

import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> readNotifications(User user) {
        return notificationRepository.readNotifications(user);
    }

    public Optional<Long> getLastReadNotificationId(User user) {
        return notificationRepository.getLastReadNotificationId(user);
    }

    public void markLastReadNotification(long lastReadNotificationId, User user) {
        notificationRepository.markLastReadNotification(lastReadNotificationId, user);
    }
}
