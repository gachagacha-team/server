package gachagacha.storageredis;

import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class NotificationRedisRepository implements NotificationRepository {

    private final RedisTemplate<String, Long> longRedisTemplate;
    private final RedisTemplate<String, Notification> notificationRedisTemplate;
    private static final String NOTIFICATION_PREFIX = "notification:";
    private static final String NOTIFICATION_READ_MARK_PREFIX = "notification:read:mark:";

    @Override
    public List<Notification> readNotifications(User user) {
        return notificationRedisTemplate.opsForList().range(NOTIFICATION_PREFIX + user.getId(), 0, -1);
    }

    @Override
    public Long saveNotification(Notification notification, User user) {
        Long notificationId = generateNotificationId();
        notification.setId(notificationId);

        notificationRedisTemplate.opsForList()
                .leftPush(NOTIFICATION_PREFIX + user.getId(), notification);

        notificationRedisTemplate.expire(NOTIFICATION_PREFIX + user.getId(), 30, TimeUnit.DAYS);
        return notificationId;
    }

    private Long generateNotificationId() {
        return longRedisTemplate.opsForValue().increment("notification:id");
    }

    @Override
    public Optional<Long> getLastReadNotificationId(User user) {
        return Optional.ofNullable(longRedisTemplate.opsForValue().get(NOTIFICATION_READ_MARK_PREFIX + user.getId()));
    }

    @Override
    public void markLastReadNotification(long lastReadNotificationId, User user) {
        longRedisTemplate.opsForValue().set(NOTIFICATION_READ_MARK_PREFIX + user.getId(), lastReadNotificationId);
    }
}
