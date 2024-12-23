package gachagacha.gachagacha.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditTradeRequest {

    private int price;
    private String status;
}
