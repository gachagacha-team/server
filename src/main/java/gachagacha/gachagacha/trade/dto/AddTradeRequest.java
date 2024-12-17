package gachagacha.gachagacha.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddTradeRequest {

    private long itemId;
    private int price;
}
