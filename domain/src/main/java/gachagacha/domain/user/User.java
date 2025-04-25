package gachagacha.domain.user;

import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private SocialType socialType;
    private Long loginId;
    private String nickname;
    private Coin coin;
    private Score score;
    private Profile profile;
    private List<Background> backgrounds = new ArrayList<>();

    private static final int GACHA_COST = 1000;

    public static User of(String nickname, SocialType socialType, Long loginId, Profile profile) {
        return new User(
                null,
                socialType,
                loginId,
                nickname,
                new Coin(),
                new Score(),
                profile,
                List.of(Background.WHITE, Background.SKYBLUE, Background.CLOUD_GROUND)
        );
    }

    public User(Long id, SocialType socialType, Long loginId, String nickname, int coin, int score, Profile profile, List<Background> backgrounds) {
        this.id = id;
        this.socialType = socialType;
        this.loginId = loginId;
        this.nickname = nickname;
        this.score = new Score(score);
        this.coin = new Coin(coin);
        this.profile = profile;
        this.backgrounds = backgrounds;
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
