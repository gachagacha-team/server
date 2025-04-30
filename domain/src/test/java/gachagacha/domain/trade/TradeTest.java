package gachagacha.domain.trade;

import gachagacha.domain.item.Item;
import gachagacha.domain.user.*;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TradeTest {

    @Test
    void 거래_완료() {
        User buyer = mock(User.class);
        when(buyer.getId()).thenReturn(1l);

        User seller = User.createInitialUser(SocialType.KAKAO, 1l, "seller", Profile.COW);
        Trade trade = Trade.createInitialTrade(seller.getId(), Item.BLACK_CAT);

        trade.processTrade(buyer);

        assertThat(trade.getBuyerId()).isNotNull();
        assertThat(trade.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.COMPLETED);
        assertThat(trade.getTransactionDate()).isNotNull();
    }
}
