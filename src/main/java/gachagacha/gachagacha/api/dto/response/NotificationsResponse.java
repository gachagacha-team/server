package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationsResponse {

    private int count;
    private List<NotificationDto> notifications;

    public static NotificationsResponse of(List<NotificationDto> notificationDtos) {
        return new NotificationsResponse(notificationDtos.size(), notificationDtos);
    }

    @Getter
    @AllArgsConstructor
    public static class NotificationDto {
        private long id;
        private NotificationType type;
        private boolean isRead;
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
        private Long lottoId;
        private boolean isWon;
        private int rewardCoin;
    }
}
