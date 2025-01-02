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
    private Minihome miniHome;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserItem> userItems = new ArrayList<>();

    private List<Background> backgrounds = new ArrayList<>();
    private String profileImageUrl;

    public void addItem(UserItem userItem) {
        Optional<UserItem> hasDuplicatedItem = userItems.stream()
                .filter(existingUserItem -> existingUserItem.getItem() == userItem.getItem())
                .findAny();

        if (!hasDuplicatedItem.isPresent()) {
            score += userItem.getItem().getItemGrade().getScore();
        }

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

    public static User create(LoginType loginType, Long loginId, String nickname, Minihome miniHome, String profileImageUrl) {
        User user = new User();
        user.loginType = loginType;
        user.loginId = loginId;
        user.nickname = nickname;
        user.coin = 20000;
        user.miniHome = miniHome;
        user.backgrounds.add(Background.WHITE);
        user.profileImageUrl = profileImageUrl;
        return user;
    }

    public void attend() {
        this.coin += 500;
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

    public void deductCoin(int productPrice) {
        if (coin < productPrice) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_COIN);
        }
        coin -= productPrice;
    }
}
