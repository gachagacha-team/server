package gachagacha.gachagacha.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadTradeResponse {

    private long itemId;
    private String itemName;
    private String itemImageUrl;
    private String grade;
    private int price;
    private String status;
    private String seller;
    private LocalDateTime transactionDate;

    public static ReadTradeResponse fromOnSaleOrCancelled(long itemId, String itemName, String itemImageUrl, String grade, int price, String status, String seller) {
        return new ReadTradeResponse(itemId, itemName, itemImageUrl, grade, price, status, seller, null);
    }

    public static ReadTradeResponse fromSold(long itemId, String itemName, String itemImageUrl, String grade, int price, String status, String seller, LocalDateTime transactionDate) {
        return new ReadTradeResponse(itemId, itemName, itemImageUrl, grade, price, status, seller, transactionDate);
    }
}
