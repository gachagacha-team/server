package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTradeResponse {

    private long itemTypeId;
    private int price;
    private int averagePrice;
}
