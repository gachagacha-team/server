package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.*;
import gachagacha.gachagacha.item.entity.Background;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.minihome.entity.Minihome;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "loginType", "loginId" }) })
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    @Column
    private Long loginId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private int coin;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Minihome miniHome;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void addBackground(Background background) {
        this.backgrounds.add(background);
        if (background.getUser() != null) {
            background.getUser().getBackgrounds().remove(background);
        }
        background.setUser(this);
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
        if (trade.getUser() != null) {
            trade.getUser().getTrades().remove(trade);
        }
        trade.setUser(this);
    }

    public static User create(LoginType loginType, Long loginId, String nickname, Minihome miniHome) {
        User user = new User();
        user.loginType = loginType;
        user.loginId = loginId;
        user.nickname = nickname;
        user.coin = 20000;
        user.miniHome = miniHome;
        return user;
    }

    public void attend() {
        this.coin += 500;
    }
}
