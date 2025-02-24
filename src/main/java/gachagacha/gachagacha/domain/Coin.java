package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
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
