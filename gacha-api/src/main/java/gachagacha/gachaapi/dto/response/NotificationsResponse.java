package gachagacha.gachaapi.dto.response;

import gachagacha.domain.notification.Notification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationsResponse {

    private int count;
    private List<NotificationDto> notifications;

    public static NotificationsResponse of(List<Notification> notifications) {
        List<NotificationsResponse.NotificationDto> notificationDtos = notifications.stream()
                .map(notification -> new NotificationDto(notification.getId(), notification.getMessage(), notification.getNotificationType().getViewName()))
                .toList();
        return new NotificationsResponse(notificationDtos.size(), notificationDtos);
    }

    @Getter
    @AllArgsConstructor
    public static class NotificationDto {
        private long id;
        private String data;
        private String notificationType;
    }
}
