package gachagacha.gachagacha.domain.lotto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Lotto {

    private Long id;
    private long userId;
    private boolean used;
    private boolean won;
    private int rewardCoin;

    public static Lotto of(long userId, boolean won, int rewardCoin) {
        return new Lotto(
                null,
                userId,
                false,
                won,
                rewardCoin
        );
    }

    public LottoEntity toLottoEntity() {
        return new LottoEntity(
                id,
                userId,
                used,
                won,
                rewardCoin
        );
    }

    public void use() {
        this.used = true;
    }
}
