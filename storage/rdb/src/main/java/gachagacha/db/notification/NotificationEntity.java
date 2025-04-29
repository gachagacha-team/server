package gachagacha.db.notification;

import gachagacha.db.BaseEntity;
import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    public static NotificationEntity fromNotification(Notification notification) {
        return new NotificationEntity(notification.getId(), notification.getUserId(), notification.getMessage(), notification.getNotificationType());
    }

    public Notification toNotification() {
        return new Notification(id, userId, message, notificationType);
    }
}
