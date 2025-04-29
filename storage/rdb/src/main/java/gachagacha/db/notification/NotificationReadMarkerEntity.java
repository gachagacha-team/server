package gachagacha.db.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification_read_marker")
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReadMarkerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_read_marker_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long lastReadNotificationId;

    public static NotificationReadMarkerEntity of(Long userId, Long lastReadNotificationId) {
        return new NotificationReadMarkerEntity(null, userId, lastReadNotificationId);
    }
}
