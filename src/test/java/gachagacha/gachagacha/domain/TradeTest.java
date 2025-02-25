package gachagacha.gachagacha.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TradeTest {

    @Mock
    private User seller;

    @Mock
    private User buyer;

    @Test
    void 상품_구매시_구매자_id_값이_매핑된다() {
        // given
        Mockito.when(seller.getId()).thenReturn(1l);
        Mockito.when(buyer.getId()).thenReturn(2l);
        Trade trade = Trade.of(seller, Item.BLACK_CAT);

        // when
        trade.processTrade(buyer);

        // then
        assertThat(trade.getBuyerId()).isEqualTo(2);
    }

    @Test
    void 상품_등록시_거래상태는_판매중이다() {
        // given
        Trade trade = Trade.of(seller, Item.BLACK_CAT);

        // when & then
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.ON_SALE);
    }

    @Test
    void 상품_구매시_거래상태가_판매완료로_변경된다() {
        // given
        Trade trade = Trade.of(seller, Item.BLACK_CAT);

        // when
        trade.processTrade(buyer);

        // then
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.COMPLETED);
    }

    @Test
    void 상품_구매시_거래일자가_매핑된다() {
        // given
        Trade trade = Trade.of(seller, Item.BLACK_CAT);

        // when
        trade.processTrade(buyer);

        // then
        assertThat(trade.getTransactionDate()).isNotNull();
    }
}
