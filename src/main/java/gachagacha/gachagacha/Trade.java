package gachagacha.gachagacha;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.ItemType;
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

    private ItemType itemType;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus status;

    @Column(nullable = false)
    private int price;

    public void setUser(User user) {
        this.user = user;
    }

    public static Trade create(ItemType itemType, int price) {
        Trade trade = new Trade();
        trade.itemType = itemType;
        trade.price = price;
        trade.status = TradeStatus.ON_SALE;
        return trade;
    }
}
