package gachagacha.gachaapi.dto.response;

import gachagacha.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReadOneProductResponse {

    private String name;
    private String grade;
    private int price;
    private int stock;
    private String imageUrl;

    public static ReadOneProductResponse of(Item item, int stock, String itemsImageApiEndpoint) {
        return new ReadOneProductResponse(
                item.getViewName(),
                item.getItemGrade().getViewName(),
                item.getItemGrade().getPrice(),
                stock,
                itemsImageApiEndpoint + item.getImageFileName());
    }
}
