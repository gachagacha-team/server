package gachagacha.gachagacha.trade.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Enumerated(value = EnumType.STRING)
    private Item item;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus tradeStatus;

    private LocalDateTime transactionDate;

    public static Trade create(User seller, Item item) {
        Trade trade = new Trade();
        trade.seller = seller;
        trade.item = item;
        trade.tradeStatus = TradeStatus.ON_SALE;
        return trade;
    }

    public void processTrade(User buyer) {
        this.buyer = buyer;
        this.tradeStatus = TradeStatus.COMPLETED;
        this.transactionDate = LocalDateTime.now().withNano(0);
    }
}
