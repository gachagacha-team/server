package gachagacha.gachagacha.trade.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private Item item;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus status;

    @Column(nullable = false)
    private int price;

    public void setUser(User user) {
        this.user = user;
    }

    public static Trade create(Item item, int price) {
        Trade trade = new Trade();
        trade.item = item;
        trade.price = price;
        trade.status = TradeStatus.ON_SALE;
        return trade;
    }
}
