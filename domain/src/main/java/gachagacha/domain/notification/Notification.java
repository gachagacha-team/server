package gachagacha.domain.notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

    private Long id;
    private Long userId;
    private String message;
    private NotificationType notificationType;

    public static Notification of(Long userId, String data, NotificationType notificationType) {
        return new Notification(null, userId, data, notificationType);
    }
}
