package gachagacha.domain.trade;

import gachagacha.domain.item.Item;
import gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Trade {

    private Long id;
    private Long sellerId;
    private Long buyerId;
    private Item item;
    private TradeStatus tradeStatus;
    private LocalDateTime transactionDate;

    public void processTrade(User buyer) {
        this.buyerId = buyer.getId();
        this.tradeStatus = TradeStatus.COMPLETED;
        this.transactionDate = LocalDateTime.now().withNano(0);
    }

    public static Trade createInitialTrade(Long sellerId, Item item) {
        return new Trade(null, sellerId, null, item, TradeStatus.ON_SALE, null);
    }
}
