package gachagacha.gachagacha.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadMyOneProductResponse {

    private long productId;
    private String name;
    private String grade;
    private String imageUrl;
    private int price;
    private String status;
    private LocalDateTime transactionDate;

    public static ReadMyOneProductResponse fromOnSale(long productId, String itemName, String grade, String itemImageUrl, int price, String status) {
        return new ReadMyOneProductResponse(productId, itemName, grade, itemImageUrl, price, status, null);
    }

    public static ReadMyOneProductResponse fromSold(long productId, String itemName, String grade, String itemImageUrl, int price, String status, LocalDateTime transactionDate) {
        return new ReadMyOneProductResponse(productId, itemName, grade, itemImageUrl, price, status, transactionDate);
    }
}
