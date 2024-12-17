package gachagacha.gachagacha.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddBackgroundTradeRequest {

    private long backgroundTypeId;
    private int price;
}
