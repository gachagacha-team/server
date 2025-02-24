package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
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
                0,
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
        if (saleItemRemainCount == 0) {
            score.decrease(saleItem.getItemGrade().getScore());
        }
    }

    public void increaseScoreByItem(Item addedItem, List<UserItem> userItems) {
        long saleItemRemainCount = userItems.stream()
                .filter(userItem -> userItem.getItem() == addedItem)
                .count();
        if (saleItemRemainCount == 0) { // 현재 아이템 개수가 0 -> 1개로 변경되며 스코어도 올라가야 함
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
                0l,
                socialType,
                loginId,
                nickname,
                coin,
                score,
                profileImage.toEntity(),
                backgrounds
        );
    }
}
