package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Coin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinResponse {

    private int coin;

    public static CoinResponse of(Coin coin) {
        return new CoinResponse(coin.getCoin());
    }
}
