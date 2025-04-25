package gachagacha.domain.lotto;

import gachagacha.domain.item.ItemGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LottoIssuanceEvent {

    private long userId;

    private ItemGrade itemGrade;
}
