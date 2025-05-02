package gachagacha.domain.user;

import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void 가챠로_1000_코인_차감() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        int coinBeforeGacha = user.getCoin().getCoin();

        user.deductCoinForGacha();

        assertThat(user.getCoin().getCoin()).isEqualTo(coinBeforeGacha - 1000);
    }

    @Test
    void 판매하는_아이템의_종류가_더이상_없는_경우_스코어가_감소된다() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        List<UserItem> userItems = List.of(new UserItem(1l, Item.BUNNIES, user.getId()));
        int scoreBeforeSale = user.getScore().getScore();
        Item saleItem = Item.BUNNIES;

        user.decreaseScoreForSaleItem(saleItem, userItems);

        assertThat(user.getScore().getScore()).isEqualTo(scoreBeforeSale - saleItem.getItemGrade().getScore());
    }

    @Test
    void 판매하는_아이템의_종류가_여전히_남은_경우_스코어가_감소되지_않는다() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        List<UserItem> userItems = List.of(new UserItem(1l, Item.BUNNIES, user.getId()),
                new UserItem(1l, Item.BUNNIES, user.getId()));
        int scoreBeforeSale = user.getScore().getScore();
        Item saleItem = Item.BUNNIES;

        user.decreaseScoreForSaleItem(saleItem, userItems);

        assertThat(user.getScore().getScore()).isEqualTo(scoreBeforeSale);
    }

    @Test
    void 아이템_추가시_추가된_아이템의_종류가_최초이면_스코어가_증가한다() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        List<UserItem> userItems = List.of(new UserItem(1l, Item.CUPCAKE, user.getId()),
                new UserItem(1l, Item.ANGEL_KIRBY, user.getId()));
        int scoreBeforeAddItem = user.getScore().getScore();
        Item addedItem = Item.BUNNIES;

        user.increaseScoreByItem(addedItem, userItems);

        assertThat(user.getScore().getScore()).isEqualTo(scoreBeforeAddItem + addedItem.getItemGrade().getScore());
    }

    @Test
    void 아이템_추가시_추가된_아이템의_종류가_이미_존재하면_스코어가_증가하지_않는다() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        List<UserItem> userItems = List.of(new UserItem(1l, Item.BUNNIES, user.getId()),
                new UserItem(1l, Item.ANGEL_KIRBY, user.getId()));
        int scoreBeforeAddItem = user.getScore().getScore();
        Item addedItem = Item.BUNNIES;

        user.increaseScoreByItem(addedItem, userItems);

        assertThat(user.getScore().getScore()).isEqualTo(scoreBeforeAddItem);
    }

    @Test
    void 사용자_정보_변경() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        String nicknameAfterUpdate = "변경 후 닉네임";
        Profile profileAfterUpdate = Profile.COW;

        user.updateUserInfo(nicknameAfterUpdate, profileAfterUpdate);

        assertThat(user.getNickname()).isEqualTo(nicknameAfterUpdate);
        assertThat(user.getProfile()).isEqualTo(profileAfterUpdate);
    }
}
