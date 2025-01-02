package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.*;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.entity.Background;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.minihome.entity.Minihome;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    @Column(nullable = false)
    private int score;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "minihome_id", nullable = false)
    private Minihome minihome;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserItem> userItems = new ArrayList<>();

    private List<Background> backgrounds = new ArrayList<>();
    private String profileImageUrl;

    public static User create(LoginType loginType, Long loginId, String nickname, Minihome minihome, String profileImageUrl) {
        User user = new User();
        user.loginType = loginType;
        user.loginId = loginId;
        user.nickname = nickname;
        user.coin = 20000;
        user.minihome = minihome;
        user.backgrounds.add(Background.WHITE);
        user.profileImageUrl = profileImageUrl;
        return user;
    }

    public void addUserItem(UserItem userItem) {
        this.userItems.add(userItem);
        if (userItem.getUser() != null) {
            userItem.getUser().getUserItems().remove(userItem);
        }
        userItem.setUser(this);
    }

    public void cancelProductAndRevertToUserItem(Item item) {
        UserItem userItem = UserItem.create(item);
        this.userItems.add(userItem);
        if (userItem.getUser() != null) {
            userItem.getUser().getUserItems().remove(userItem);
        }
        userItem.setUser(this);
    }

    public int attend() {
        int bonusCoin = new Random().nextInt(4001) + 1000;
        this.coin += bonusCoin;
        return bonusCoin;
    }

    public void addCoin(int price) {
        coin += price;
    }

    public void deductCoinForGacha() {
        if (coin < 1000) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_COIN);
        }
        coin -= 1000;
    }

    public void purchaseProduct(int productPrice) {
        if (coin < productPrice) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_COIN);
        }
        coin -= productPrice;
    }

    public void saleUserItem(UserItem userItem) {
        validateSaleAuthorization(userItem);
        decreaseScoreForSaleItem(userItem);
    }

    private void validateSaleAuthorization(UserItem userItem) {
        if (userItem.getUser().getId() != this.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void decreaseScoreForSaleItem(UserItem userItemForSale) {
        Item item = userItemForSale.getItem();
        long itemCount = userItems.stream()
                .filter(userItem -> userItem.getItem() == item)
                .count();
        if (itemCount == 1) {
            score -= userItemForSale.getItem().getItemGrade().getScore();
        }
    }

    public void addScoreForNewItem(Item item) {
        Optional<UserItem> hasDuplicatedItem = userItems.stream()
                .filter(existingUserItem -> existingUserItem.getItem() == item)
                .findAny();

        if (!hasDuplicatedItem.isPresent()) {
            score += item.getItemGrade().getScore();
        }
    }
}
