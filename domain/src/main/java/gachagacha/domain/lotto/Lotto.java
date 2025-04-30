package gachagacha.domain.lotto;

import gachagacha.domain.item.ItemGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Lotto {

    private Long id;
    private long userId;
    private ItemGrade itemGrade;
    private boolean used;
    private boolean won;
    private int rewardCoin;

    public static Lotto createInitialLotto(Long userId, ItemGrade itemGrade, boolean won, int rewardCoin) {
        return new Lotto(null, userId, itemGrade, false, won, rewardCoin);
    }

    public void use() {
        this.used = true;
    }
}
