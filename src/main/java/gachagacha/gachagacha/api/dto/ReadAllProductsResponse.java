package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.item.Item;
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

    public static ReadAllProductsResponse of(Item item, int stock, String itemsImageApiEndpoint) {
        return new ReadAllProductsResponse(
                item.getItemId(),
                itemsImageApiEndpoint + item.getImageFileName(),
                stock > 0
        );
    }
}
