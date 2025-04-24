package gachagacha.domain.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coin {

    private int coin;
    private static final int INITIAL_COIN_AMOUNT = 20000;

    public Coin() {
        this.coin = INITIAL_COIN_AMOUNT;
    }

    public void addCoin(int bonusCoin) {
        coin += bonusCoin;
    }

    public void deduct(int amount) {
        if (coin < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_COIN);
        }
        coin -= amount;
    }
}
