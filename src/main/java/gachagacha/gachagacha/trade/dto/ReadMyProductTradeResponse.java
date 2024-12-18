package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadMyProductTradeResponse {

    private String tradeStatus;
    private String itemName;
    private String itemImageUrl;
    private int itemLevel;
    private int price;
}
