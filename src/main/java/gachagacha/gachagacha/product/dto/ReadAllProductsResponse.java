package gachagacha.gachagacha.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadAllProductsResponse {

    private long itemId;
    private String imageUrl;
    private boolean hasStock;
}
