package gachagacha.gachagacha.domain.notification;

import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final RedisTemplate<String, Long> longRedisTemplate;
    private final RedisTemplate<String, Boolean> booleanRedisTemplate;
    private final RedisTemplate<String, Notification> notificationRedisTemplate;
    private static final String NOTIFICATION_PREFIX = "notification:";
    private static final String NOTIFICATION_READ_KEY = "notification:read";

    public List<Notification> readNotifications(User user) {
        return notificationRedisTemplate.opsForList().range(NOTIFICATION_PREFIX + user.getId(), 0, -1);
    }

    public boolean isRead(long notificationId) {
        return (Boolean) booleanRedisTemplate.opsForHash().get(NOTIFICATION_READ_KEY, notificationId);
    }

    public Long saveNotification(Notification notification, User user) {
        Long notificationId = generateNotificationId();
        notification.setId(notificationId);

        notificationRedisTemplate.opsForList()
                .leftPush(NOTIFICATION_PREFIX + user.getId(), notification);

        booleanRedisTemplate.opsForHash()
                        .put(NOTIFICATION_READ_KEY,
                                notificationId,
                                false);
        return notificationId;
    }

    private Long generateNotificationId() {
        return longRedisTemplate.opsForValue().increment("notification:id");
    }

    public void readOneNotification(long notificationId) {
        if (booleanRedisTemplate.opsForHash().hasKey(NOTIFICATION_READ_KEY, notificationId)) {
            booleanRedisTemplate.opsForHash().put(
                    NOTIFICATION_READ_KEY,
                    notificationId,
                    true
            );
        }
    }
}
