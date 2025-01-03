package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadItemForSaleResponse {

    private long itemId;
    private String name;
    private String grade;
    private int price;
}
