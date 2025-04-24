package gachagacha.db.trade;

import gachagacha.db.BaseEntity;
import gachagacha.domain.trade.Trade;
import gachagacha.domain.item.Item;
import gachagacha.domain.trade.TradeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "trade")
@NoArgsConstructor
@AllArgsConstructor
public class TradeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    private Long buyerId;

    @Enumerated(value = EnumType.STRING)
    private Item item;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus tradeStatus;

    private LocalDateTime transactionDate;

    public static TradeEntity fromTrade(Trade trade) {
        return new TradeEntity(trade.getId(), trade.getSellerId(), trade.getBuyerId(),
                trade.getItem(), trade.getTradeStatus(), trade.getTransactionDate());
    }

    public Trade toTrade() {
        return new Trade(id, sellerId, buyerId, item, tradeStatus, transactionDate);
    }

    public void updateFromTrade(Trade trade) {
        this.sellerId = trade.getSellerId();
        this.buyerId = trade.getBuyerId();
        this.item = trade.getItem();
        this.tradeStatus = trade.getTradeStatus();
        this.transactionDate = trade.getTransactionDate();
    }
}
