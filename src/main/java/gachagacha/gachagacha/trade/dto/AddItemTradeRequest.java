package gachagacha.gachagacha.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddItemTradeRequest {

    private long itemId;
    private int price;
}
