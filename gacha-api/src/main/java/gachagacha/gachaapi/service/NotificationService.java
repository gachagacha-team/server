package gachagacha.gachaapi.service;

import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public boolean hasNewNotification(User user) {
        return notificationRepository.hasNewNotification(user.getId());
    }

    @Transactional
    public List<Notification> readRecentNotifications(User user) {
        notificationRepository.markLastReadNotification(user);
        return notificationRepository.readRecentNotifications(user);
    }
}
