package gachagacha.gachagacha.api.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LottoResponse {

    private Long id;
    private boolean won;
    private int rewardCoin;
}
