package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.lotto.Lotto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadLottoResponse {

    private Long lottoId;
    private boolean won;
    private int rewardCoin;

    public static ReadLottoResponse of(Lotto lotto) {
        return new ReadLottoResponse(lotto.getId(), lotto.isWon(), lotto.getRewardCoin());
    }
}
