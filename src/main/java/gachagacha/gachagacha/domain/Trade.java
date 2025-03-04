package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.TradeEntity;
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

    public static Trade of(User seller, Item item) {
        return new Trade(
                null,
                seller.getId(),
                null,
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
