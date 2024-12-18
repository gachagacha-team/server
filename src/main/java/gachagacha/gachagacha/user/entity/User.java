package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.*;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.entity.UserItem;
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
    private List<UserItem> userItems = new ArrayList<>();

    public void addItem(UserItem userItem) {
        this.userItems.add(userItem);
        if (userItem.getUser() != null) {
            userItem.getUser().getUserItems().remove(userItem);
        }
        userItem.setUser(this);
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

    public void deductCoin(int price) {
        if (coin < price) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_COIN);
        }
        coin -= price;
    }

    public void addCoin(int price) {
        coin += price;
    }
}
