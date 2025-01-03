package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.*;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.entity.Background;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.trade.entity.Trade;
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
    @Embedded
    private Coin coin;

    @Column(nullable = false)
    @Embedded
    private Score score;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "minihome_id", nullable = false)
    private Minihome minihome;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserItem> userItems = new ArrayList<>();

    private List<Background> backgrounds = new ArrayList<>();
    private String profileImageUrl;
    private static final int GACHA_COST = 1000;

    public static User create(LoginType loginType, Long loginId, String nickname, String profileImageUrl) {
        User user = new User();
        user.loginType = loginType;
        user.loginId = loginId;
        user.nickname = nickname;
        user.coin = new Coin();
        user.score = new Score();
        user.minihome = Minihome.create();
        user.backgrounds.add(Background.WHITE);
        user.profileImageUrl = profileImageUrl;
        return user;
    }

    public void addUserItem(UserItem userItem) {
        score.addScoreForNewItem(userItem.getItem(), userItems);

        userItems.add(userItem);
        if (userItem.getUser() != null) {
            userItem.getUser().getUserItems().remove(userItem);
        }
        userItem.setUser(this);
    }

    public void deductCoinForGacha() {
        coin.deduct(GACHA_COST);
    }

    /* 판매 등록 */
    public void saleUserItem(UserItem userItem) {
        if (userItem.getUser().getId() != this.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        userItems.remove(userItem);
        score.decreaseScoreForSaleItem(userItems, userItem.getItem());
    }

    /* 판매 취소 */
    public void cancelTrade(Trade trade) {
        if (trade.getSeller().getId() != this.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        UserItem userItem = UserItem.create(trade.getItem());
        this.addUserItem(userItem);
    }

    /* 구매자: 구매 */
    public void processPurchase(int price, Item item) {
        coin.deduct(price);
        addUserItem(UserItem.create(item));
    }

    /* 판매자: 판매 완료 */
    public void completeSale(int price) {
        coin.addCoin(price);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addCoin(int bonusCoin) {
        this.coin.addCoin(bonusCoin);
    }

    public int getCoinAmount() {
        return this.getCoin().getCoin();
    }
}
