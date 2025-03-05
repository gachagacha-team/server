package gachagacha.gachagacha.domain.lotto.dto;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class IssuedLotto implements Serializable {

    private long userId;
    private boolean won;
    private int rewardCoin;
}
