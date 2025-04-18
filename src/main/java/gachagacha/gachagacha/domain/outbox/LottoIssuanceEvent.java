package gachagacha.gachagacha.domain.outbox;

import gachagacha.gachagacha.domain.item.ItemGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LottoIssuanceEvent {

    private long userId;

    private ItemGrade itemGrade;
}
