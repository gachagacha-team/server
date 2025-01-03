package gachagacha.gachagacha;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.user.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void 회원가입시_20000_코인이_지급된다() {
        // given & when
        User user = User.create(LoginType.KAKAO, 1l, "nickname","profileImageUrl");

        // then
        assertThat(user.getCoinAmount()).isEqualTo(20000);
    }

    @Test
    void 아이템이_추가되면_양방향_연관관계가_매핑된다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname","profileImageUrl");
        Item item = Item.BLACK_CAT;
        UserItem userItem = UserItem.create(item);

        // when
        user.addUserItem(userItem);

        // then
        assertThat(user.getUserItems().size()).isEqualTo(1);
        assertThat(user.getUserItems().get(0).getItem()).isEqualTo(item);
        assertThat(userItem.getUser()).isEqualTo(user);
        assertThat(userItem.getUser().getUserItems().size()).isEqualTo(1);
    }

    @Test
    void 아이템이_추가될_때_새로운_아이템이면_스코어가_올라간다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname","profileImageUrl");
        Item item = Item.BLACK_CAT;
        UserItem userItem = UserItem.create(item);
        int existingScore = user.getScore().getScore();

        // when
        user.addUserItem(userItem);

        // then
        assertThat(user.getScore().getScore()).isEqualTo(existingScore + item.getItemGrade().getScore());
    }

    @Test
    void 아이템이_추가될_때_이미_소유한_아이템이면_스코어가_올라가지_않는다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname","profileImageUrl");
        Item item = Item.BLACK_CAT;
        user.addUserItem(UserItem.create(item));
        int existingScore = user.getScore().getScore();

        // when
        user.addUserItem(UserItem.create(item));

        // then
        assertThat(user.getScore().getScore()).isEqualTo(existingScore);
    }

    @Test
    void 뽑기시_코인이_부족하면_예외가_발생한다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname", "profileImageUrl");

        for (int i = 0; i < 20; i++) { // 초기 코인이 20,000이므로 코인 차감(-1,000)을 20번 반복하여 코인을 부족하게 만든다.
            user.deductCoinForGacha();
        }

        // when & then
        assertThatThrownBy(() -> user.deductCoinForGacha())
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_COIN.getMessage());
    }

    @Test
    void 뽑기시_1000_코인이_차감된다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname", "profileImageUrl");
        int coin = user.getCoinAmount();

        // when
        user.deductCoinForGacha();

        // then
        assertThat(user.getCoinAmount()).isEqualTo(coin - 1000);
    }

    @Test
    void 자신이_소유한_아이템이_아니면_판매가_불가하다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller", "profileImageUrl");
        User itemOwner = User.create(LoginType.KAKAO, 1l, "itemOwner", "profileImageUrl");
        UserItem userItem = UserItem.create(Item.BLACK_CAT);
        itemOwner.addUserItem(userItem);

        seller.setId(1l);
        itemOwner.setId(2l);

        // when & then
        assertThatThrownBy(() -> seller.saleUserItem(userItem))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED.getMessage());
    }

    @Test
    void 상품_등록_후_소유한_아이템이_제거된다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller", "profileImageUrl");
        UserItem userItem = UserItem.create(Item.BLACK_CAT);
        seller.addUserItem(userItem);

        // when
        seller.saleUserItem(userItem);

        // then
        assertThat(seller.getUserItems()).isEmpty();
    }

    @Test
    void 상품_등록_후_해당_아이템이_더이상_존재하지_않으면_스코어가_내려간다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller", "profileImageUrl");
        UserItem userItem = UserItem.create(Item.BLACK_CAT);
        seller.addUserItem(userItem);
        int existingScore = seller.getScore().getScore();

        // when
        seller.saleUserItem(userItem);

        // then
        assertThat(seller.getScore().getScore()).isEqualTo(existingScore - userItem.getItem().getItemGrade().getScore());
    }

    @Test
    void 상품_등록_후에도_해당_아이템을_소유하면_스코어가_내려가지_않는다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller", "profileImageUrl");

        UserItem userItem1 = UserItem.create(Item.BLACK_CAT);
        seller.addUserItem(userItem1);

        UserItem userItem2 = UserItem.create(Item.BLACK_CAT);
        seller.addUserItem(userItem2);

        int existingScore = seller.getScore().getScore();

        // when
        seller.saleUserItem(userItem1);

        // then
        assertThat(seller.getScore().getScore()).isEqualTo(existingScore);
    }

    @Test
    void 자신이_판매자가_아니면_판매_취소가_불가하다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller", "profileImageUrl");
        Trade trade = Trade.create(seller, Item.BLACK_CAT);

        User currentUser = User.create(LoginType.KAKAO, 2l, "currentUser", "profileImageUrl");

        seller.setId(1l);
        currentUser.setId(2l);

        // when & then
        assertThatThrownBy(() -> currentUser.cancelTrade(trade))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED.getMessage());
    }

    @Test
    void 판매_취소_후_아이템이_다시_지급된다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller", "profileImageUrl");
        Item item = Item.BLACK_CAT;
        Trade trade = Trade.create(seller, item);

        // when
        seller.cancelTrade(trade);

        // then
        assertThat(seller.getUserItems().size()).isEqualTo(1);
        assertThat(seller.getUserItems().get(0).getUser()).isEqualTo(seller);
        assertThat(seller.getUserItems().get(0).getItem()).isEqualTo(item);
    }

    @Test
    void 코인이_부족하면_상품_구매에_실패한다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname", "profileImageUrl");

        // when
        for (int i = 0; i < 20; i++) { // 초기 코인이 20,000이므로 코인 차감(-1,000)을 20번 반복하여 코인을 부족하게 만든다.
            user.deductCoinForGacha();
        }

        // then
        assertThatCode(() -> user.processPurchase(Item.BLACK_CAT))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_COIN.getMessage());
    }

    @Test
    void 상품_구매_후_상품_금액만큼_코인이_차감된다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname", "profileImageUrl");
        Item item = Item.BLACK_CAT;
        int productPrice = item.getItemGrade().getPrice();
        int initialCoin = user.getCoinAmount();

        // when
        user.processPurchase(item);

        // then
        assertThat(user.getCoinAmount()).isEqualTo(initialCoin - productPrice);
    }

    @Test
    void 상품_구매_후_아이템이_지급된다() {
        // given
        User user = User.create(LoginType.KAKAO, 1l, "nickname","profileImageUrl");
        Item item = Item.BLACK_CAT;

        // when
        user.processPurchase( item);

        // then
        assertThat(user.getUserItems().size()).isEqualTo(1);
        assertThat(user.getUserItems().get(0).getItem()).isEqualTo(item);
    }
}
