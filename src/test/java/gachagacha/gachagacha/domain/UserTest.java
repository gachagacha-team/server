package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItem;
import gachagacha.gachagacha.domain.guestbook.ProfileImage;
import gachagacha.gachagacha.domain.user.SocialType;
import gachagacha.gachagacha.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void 뽑기시_1000_코인이_차감된다() {
        // given
        User user = User.of("user", SocialType.KAKAO, 1l, ProfileImage.of(null));
        int initialCoinAmount = user.getCoin().getCoin();

        // when
        user.deductCoinForGacha();

        // then
        assertThat(user.getCoin().getCoin()).isEqualTo(initialCoinAmount - 1000);
    }

    @Test
    void 아이템을_상품으로_등록하여_해당_아이템이_더이상_없으면_스코어가_떨어진다() {
        // given
        User user = User.of("user", SocialType.KAKAO, 1l, ProfileImage.of(null));
        user.increaseScoreByItem(Item.BLACK_CAT, List.of());
        user.increaseScoreByItem(Item.ANGEL_KIRBY, List.of(UserItem.of(user, Item.BLACK_CAT)));
        int previousScore = user.getScore().getScore();

        List<UserItem> userItems = List.of(
                UserItem.of(user, Item.BLACK_CAT),
                UserItem.of(user, Item.ANGEL_KIRBY)
        );
        Item saleItem = Item.BLACK_CAT;

        // when
        user.decreaseScoreForSaleItem(saleItem, userItems);

        // then
        assertThat(user.getScore().getScore()).isEqualTo(previousScore - saleItem.getItemGrade().getScore());
    }

    @Test
    void 아이템을_상품으로_등록하더라도_해당_아이템을_여전히_보유중이면_스코어가_떨어지지_않는다() {
        // given
        User user = User.of("user", SocialType.KAKAO, 1l, ProfileImage.of(null));
        user.increaseScoreByItem(Item.BLACK_CAT, List.of());
        user.increaseScoreByItem(Item.BLACK_CAT, List.of(UserItem.of(user, Item.BLACK_CAT)));
        user.increaseScoreByItem(Item.ANGEL_KIRBY, List.of(UserItem.of(user, Item.BLACK_CAT), UserItem.of(user, Item.BLACK_CAT)));
        int previousScore = user.getScore().getScore();

        List<UserItem> userItems = List.of(
                UserItem.of(user, Item.BLACK_CAT),
                UserItem.of(user, Item.BLACK_CAT),
                UserItem.of(user, Item.ANGEL_KIRBY)
        );
        Item saleItem = Item.BLACK_CAT;

        // when
        user.decreaseScoreForSaleItem(saleItem, userItems);

        // then
        assertThat(user.getScore().getScore()).isEqualTo(previousScore);
    }

    @Test
    void 기존에_없던_아이템이_추가되면_스코어가_올라간다() {
        // given
        User user = User.of("user", SocialType.KAKAO, 1l, ProfileImage.of(null));
        int previousScore = user.getScore().getScore();

        List<UserItem> userItems = List.of(UserItem.of(user, Item.BLACK_CAT),
                UserItem.of(user, Item.ANGEL_KIRBY)
        );
        Item newItem = Item.BUNNIES;

        // when
        user.increaseScoreByItem(newItem, userItems);

        // then
        assertThat(user.getScore().getScore()).isEqualTo(previousScore + newItem.getItemGrade().getScore());
    }

    @Test
    void 아이템이_추가되더라도_기존에_보유한_아이템이면_스코어가_올라가지_않는다() {
        // given
        User user = User.of("user", SocialType.KAKAO, 1l, ProfileImage.of(null));
        int previousScore = user.getScore().getScore();

        List<UserItem> userItems = List.of(
                UserItem.of(user, Item.BLACK_CAT),
                UserItem.of(user, Item.BUNNIES),
                UserItem.of(user, Item.ANGEL_KIRBY)
        );
        Item newItem = Item.BUNNIES;

        // when
        user.increaseScoreByItem(newItem, userItems);

        // then
        assertThat(user.getScore().getScore()).isEqualTo(previousScore);
    }
}
