package gachagacha.gachagacha.domain.item.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.trade.Trade;
import gachagacha.gachagacha.domain.trade.TradeStatus;
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
