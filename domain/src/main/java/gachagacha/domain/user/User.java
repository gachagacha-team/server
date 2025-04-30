package gachagacha.domain.user;

import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private Long id;
    private SocialType socialType;
    private Long loginId;
    private String nickname;
    private Coin coin;
    private Score score;
    private Profile profile;

    private static final int GACHA_COST = 1000;

    public static User of(String nickname, SocialType socialType, Long loginId, Profile profile) {
        return new User(
                null,
                socialType,
                loginId,
                nickname,
                Coin.of(),
                Score.of(),
                profile
        );
    }

    public void deductCoinForGacha() {
        coin.deduct(GACHA_COST);
    }

    public void decreaseScoreForSaleItem(Item saleItem, List<UserItem> userItems) {
        long saleItemRemainCount = userItems.stream()
                .filter(userItem -> userItem.getItem() == saleItem)
                .count();
        if (saleItemRemainCount == 1) {
            score.decrease(saleItem.getItemGrade().getScore());
        }
    }

    public void increaseScoreByItem(Item addedItem, List<UserItem> userItems) {
        long saleItemRemainCount = userItems.stream()
                .filter(userItem -> userItem.getItem() == addedItem)
                .count();
        if (saleItemRemainCount == 0) {
            score.increase(addedItem.getItemGrade().getScore());
        }
    }

    public void deductCoin(int price) {
        this.coin.deduct(price);
    }

    public void addCoin(int quantity) {
        this.coin.addCoin(quantity);
    }

    public void updateUserInfo(String nickname, Profile profile) {
        this.nickname = nickname;
        this.profile = profile;
    }
}
