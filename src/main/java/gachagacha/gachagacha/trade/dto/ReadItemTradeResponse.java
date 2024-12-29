package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadItemTradeResponse {

    private long itemId;
    private String itemName;
    private String grade;
    private int averagePrice;
}
