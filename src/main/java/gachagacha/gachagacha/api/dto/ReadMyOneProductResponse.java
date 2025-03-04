package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.trade.Trade;
import gachagacha.gachagacha.domain.trade.TradeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReadMyOneProductResponse {

    private long productId;
    private String imageUrl;
    private String name;
    private String grade;
    private int price;
    private String status;
    private LocalDateTime transactionDate;

    public static ReadMyOneProductResponse of(Trade trade, String itemsImageApiEndpoint) {
        ReadMyOneProductResponse response = new ReadMyOneProductResponse();
        response.productId = trade.getId();
        response.imageUrl = itemsImageApiEndpoint + trade.getItem().getImageFileName();
        response.name = trade.getItem().getViewName();
        response.grade = trade.getItem().getItemGrade().getViewName();
        response.price = trade.getItem().getItemGrade().getPrice();
        response.status = trade.getTradeStatus().getViewName();
        if (trade.getTradeStatus() == TradeStatus.COMPLETED) {
            response.transactionDate = trade.getTransactionDate();
        }
        return response;
    }
}
