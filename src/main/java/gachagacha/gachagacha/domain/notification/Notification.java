package gachagacha.gachagacha.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private Long id;
    private NotificationType type;
    private Object data;

    public void setId(Long id) {
        this.id = id;
    }

    public static Notification of(NotificationType type, Object data) {
        return new Notification(
                null,
                type,
                data
        );
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LottoIssuedNotification {

        private Long lottoId;
        private boolean won;
        private int rewardCoin;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TradeCompletedNotification {

        private String itemName;
        private int coin;
    }
}
