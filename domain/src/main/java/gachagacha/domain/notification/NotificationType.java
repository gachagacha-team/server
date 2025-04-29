package gachagacha.domain.notification;

import gachagacha.domain.item.Item;
import gachagacha.domain.item.ItemGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    LOTTO_ISSUED("lotto_issued") {
        @Override
        public String generateNotificationMessageByLottoIssued(ItemGrade itemGrade) {
            return itemGrade.getViewName() + "등급 아이템을 모두 모아 복권이 발급되었습니다!";
        }

        @Override
        public String generateNotificationMessageByTradeCompleted(Item item) {
            throw new UnsupportedOperationException(this.name() + "에서는 해당 메서드를 호출할 수 없습니다.");
        }
    },
    TRADE_COMPLETED("trade_completed") {
        @Override
        public String generateNotificationMessageByTradeCompleted(Item item) {
            return item.getViewName() + " 아이템이 판매되어 " + item.getItemGrade().getPrice() + " 코인이 지급되었습니다.";
        }

        @Override
        public String generateNotificationMessageByLottoIssued(ItemGrade itemGrade) {
            throw new UnsupportedOperationException(this.name() + "에서는 해당 메서드를 호출할 수 없습니다.");
        }
    };

    private final String viewName;

    public abstract String generateNotificationMessageByLottoIssued(ItemGrade itemGrade);

    public abstract String generateNotificationMessageByTradeCompleted(Item item);
}
