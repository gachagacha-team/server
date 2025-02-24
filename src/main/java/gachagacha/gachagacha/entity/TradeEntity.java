package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.Item;
import gachagacha.gachagacha.domain.Trade;
import gachagacha.gachagacha.domain.TradeStatus;
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
    private long id;

    @Column(nullable = false)
    private long sellerId;

    private long buyerId;

    @Enumerated(value = EnumType.STRING)
    private Item item;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus tradeStatus;

    private LocalDateTime transactionDate;

    public static TradeEntity create(long sellerId, Item item) {
        TradeEntity trade = new TradeEntity();
        trade.sellerId = sellerId;
        trade.item = item;
        trade.tradeStatus = TradeStatus.ON_SALE;
        return trade;
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
