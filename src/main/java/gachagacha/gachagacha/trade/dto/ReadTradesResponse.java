package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadTradesResponse {

    private long tradeId;
    private long itemId;
    private String itemName;
    private String itemImageUrl;
    private String grade;
    private int price;
    private String status;
}
