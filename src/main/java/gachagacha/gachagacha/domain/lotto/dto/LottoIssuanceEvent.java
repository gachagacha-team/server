package gachagacha.gachagacha.domain.lotto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class LottoIssuanceEvent implements Serializable {

    private Long userId;
    private String itemGrade;
}
