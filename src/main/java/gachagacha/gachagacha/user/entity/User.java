package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.*;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "home_id", nullable = false)
    private MiniHome miniHome;

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

    public static User create(LoginType loginType, Long loginId, String nickname, MiniHome miniHome) {
        User user = new User();
        user.loginType = loginType;
        user.loginId = loginId;
        user.nickname = nickname;
        user.coin = 20000;
        user.miniHome = miniHome;
        return user;
    }
}
