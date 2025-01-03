package gachagacha.gachagacha;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.trade.entity.TradeStatus;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.user.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TradeTest {

    @Test
    void 상품이_판매되면_구매자가_매핑되고_거래상태가_완료로_변경된다() {
        // given
        User seller = User.create(LoginType.KAKAO, 1l, "seller","profileImageUrl");
        Trade trade = Trade.create(seller, Item.BLACK_CAT);

        User buyer = User.create(LoginType.KAKAO, 2l, "buyer","profileImageUrl");

        // when
        trade.processTrade(buyer);

        // then
        assertThat(trade.getBuyer()).isEqualTo(buyer);
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.COMPLETED);
    }
}
