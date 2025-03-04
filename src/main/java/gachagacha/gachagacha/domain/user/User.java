package gachagacha.gachagacha.domain.user;

import gachagacha.gachagacha.domain.guestbook.ProfileImage;
import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItem;
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
    private ProfileImage profileImage;
    private List<Background> backgrounds = new ArrayList<>();

    private static final int GACHA_COST = 1000;

    public static User of(String nickname, SocialType socialType, Long loginId, ProfileImage profileImage) {
        return new User(
                null,
                socialType,
                loginId,
                nickname,
                new Coin(),
                new Score(),
                profileImage,
                List.of(Background.WHITE, Background.SKYBLUE, Background.CLOUD_GROUND)
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

    public UserEntity toUserEntity() {
        return new UserEntity(
                id,
                socialType,
                loginId,
                nickname,
                coin,
                score,
                profileImage,
                backgrounds
        );
    }
}
