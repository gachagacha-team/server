package gachagacha.gachagacha.product.dto;

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

    public ReadMyOneProductResponse(long productId, String imageUrl, String name, String grade, int price, String status) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.grade = grade;
        this.price = price;
        this.status = status;
        this.transactionDate = null;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
