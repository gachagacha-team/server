package gachagacha.domain.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoinTest {

    @Test
    void 최초_초기화되는_코인은_20000() {
        Coin coin = Coin.createInitialCoin();

        assertThat(coin.getCoin()).isEqualTo(20000);
    }

    @Test
    void 코인_추가() {
        Coin coin = Coin.createInitialCoin();
        int initialCoinAmount = coin.getCoin();

        coin.addCoin(1000);

        assertThat(coin.getCoin()).isEqualTo(initialCoinAmount + 1000);
    }

    @Test
    void 코인_차감() {
        Coin coin = Coin.createInitialCoin();
        int initialCoinAmount = coin.getCoin();

        coin.deduct(1000);

        assertThat(coin.getCoin()).isEqualTo(initialCoinAmount - 1000);
    }

    @Test
    void 코인_차감시_코인이_부족하면_예외_발생() {
        Coin coin = Coin.createInitialCoin();
        int initialCoinAmount = coin.getCoin();

        assertThatThrownBy(() -> coin.deduct(initialCoinAmount + 1000))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_COIN.getMessage());
    }
}
