package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {

    private long tradeId;
    private String sellerNickname;
    private String buyerNickname;
    private String item;
    private int price;
    private String tradeStatus;
}
