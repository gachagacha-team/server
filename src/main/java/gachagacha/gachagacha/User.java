package gachagacha.gachagacha;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int coin;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    private List<Background> backgrounds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trade> trades = new ArrayList<>();

    public void addItem(Item item) {
        this.items.add(item);
        if (item.getUser() != null) {
            item.getUser().getItems().remove(item);
        }
        item.setUser(this);
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
        if (trade.getUser() != null) {
            trade.getUser().getTrades().remove(trade);
        }
        trade.setUser(this);
    }
}
