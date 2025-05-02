package gachagacha.gachaapi.dto.response;

import gachagacha.domain.lotto.Lotto;
import gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LottoUsageResponse {

    private boolean won;
    private int rewardCoin;
    private int totalCoin;

    public LottoUsageResponse(User user, Lotto lotto) {
        this.won = lotto.isWon();
        this.rewardCoin = lotto.getRewardCoin();
        this.totalCoin = user.getCoin().getCoin();
    }
}
