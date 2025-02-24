package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.TradeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Trade {

    private long id;
    private long sellerId;
    private long buyerId;
    private Item item;
    private TradeStatus tradeStatus;
    private LocalDateTime transactionDate;

    public static Trade of(User seller, Item item) {
        return new Trade(
                0l,
                seller.getId(),
                0l,
                item,
                TradeStatus.ON_SALE,
                null
        );
    }

    public void processTrade(User buyer) {
        this.buyerId = buyer.getId();
        this.tradeStatus = TradeStatus.COMPLETED;
        this.transactionDate = LocalDateTime.now().withNano(0);
    }

    public void softDeleteBySeller() {
        sellerId = -1;
    }

    public void softDeleteByBuyer() {
        buyerId = -1;
    }

    public TradeEntity toTradeEntity() {
        return new TradeEntity(
                id,
                sellerId,
                buyerId,
                item,
                tradeStatus,
                getTransactionDate()
        );
    }
}
