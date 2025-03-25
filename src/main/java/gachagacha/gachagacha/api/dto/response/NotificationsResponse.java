package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.notification.Notification;
import gachagacha.gachagacha.domain.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationsResponse {

    private boolean hasNewNotification;
    private int count;
    private List<NotificationDto> notifications;

    public static NotificationsResponse of(boolean hasNewNotification, List<Notification> notifications) {
        List<NotificationsResponse.NotificationDto> notificationDtos = notifications.stream()
                .map(notification -> new NotificationDto(notification.getId(), notification.getType(), notification.getData()))
                .toList();
        return new NotificationsResponse(hasNewNotification, notificationDtos.size(), notificationDtos);
    }

    @Getter
    @AllArgsConstructor
    public static class NotificationDto {
        private long id;
        private NotificationType type;
        private Object data;
    }

    @Getter
    @AllArgsConstructor
    public static class TradeCompletedNotification {
        private String itemName;
        private int coin;
    }

    @Getter
    @AllArgsConstructor
    public static class LottoIssuedNotification {
        private String itemGrade;
    }
}
