package gachagacha.domain.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coin {

    private int coin;
    private static final int INITIAL_COIN_AMOUNT = 20000;

    public static Coin of() {
        return new Coin(INITIAL_COIN_AMOUNT);
    }

    public void addCoin(int amount) {
        coin += amount;
    }

    public void deduct(int amount) {
        System.out.println(coin + " , " + amount);
        if (coin < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_COIN);
        }
        coin -= amount;
    }
}
