package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Item;
import gachagacha.gachagacha.domain.Trade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadOneProductResponse {

    private String name;
    private String grade;
    private int stock;
    private String imageUrl;

    public static ReadOneProductResponse of(Item item, int stock, String itemsImageApiEndpoint) {
        return new ReadOneProductResponse(
                item.getViewName(),
                item.getItemGrade().getViewName(),
                stock,
                itemsImageApiEndpoint + item.getImageFileName());
    }
}
