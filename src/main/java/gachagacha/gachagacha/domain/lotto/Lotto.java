package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.item.ItemGrade;
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

    public void use() {
        this.used = true;
    }
}
