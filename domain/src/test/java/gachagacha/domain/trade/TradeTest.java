package gachagacha.domain.trade;

import gachagacha.domain.item.Item;
import gachagacha.domain.user.Profile;
import gachagacha.domain.user.SocialType;
import gachagacha.domain.user.User;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TradeTest {


    @Test
    void 거래_완료() {
        User buyer = mock(User.class);
        when(buyer.getId()).thenReturn(1l);

        User seller = User.of("seller", SocialType.GITHUB, 1l, Profile.BEAR);
        Trade trade = Trade.of(seller, Item.BLACK_CAT);

        assertThat(trade.getBuyerId()).isNull();
        trade.processTrade(buyer);

        assertThat(trade.getBuyerId()).isNotNull();
        assertThat(trade.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.COMPLETED);
        assertThat(trade.getTransactionDate()).isNotNull();
    }
}
