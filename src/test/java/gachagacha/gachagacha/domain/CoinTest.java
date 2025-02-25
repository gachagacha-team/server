package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoinTest {

    @Test
    void 코인_초기값은_20000이다() {
        // given
        Coin coin = new Coin();

        // when & then
        assertThat(coin.getCoin()).isEqualTo(20000);
    }

    @Test
    void 코인_추가() {
        // given
        Coin coin = new Coin();
        int initialCoinAmount = coin.getCoin();
        int addedCoinAmount = 10000;

        // when
        coin.addCoin(addedCoinAmount);

        // then
        assertThat(coin.getCoin()).isEqualTo(initialCoinAmount + addedCoinAmount);
    }

    @Test
    void 코인_차감() {
        // given
        Coin coin = new Coin();
        int initialCoinAmount = coin.getCoin();
        int deductedCoinAmount = 10000;

        // when
        coin.deduct(deductedCoinAmount);

        // then
        assertThat(coin.getCoin()).isEqualTo(initialCoinAmount - deductedCoinAmount);
    }

    @Test
    void 코인_차감시_코인이_부족하면_예외가_발생한다() {
        // given
        Coin coin = new Coin();
        int initialCoinAmount = coin.getCoin();
        int deductedCoinAmount = initialCoinAmount + 10000;

        // when & then
        assertThatThrownBy(() -> coin.deduct(deductedCoinAmount))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_COIN.getMessage());
    }
}
