package gachagacha.gachaapi.dto.response;

import gachagacha.domain.user.Coin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CoinResponse {

    private int coin;

    public static CoinResponse of(Coin coin) {
        return new CoinResponse(coin.getCoin());
    }
}
