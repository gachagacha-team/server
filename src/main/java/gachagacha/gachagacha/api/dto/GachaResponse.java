package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GachaResponse {

    private String itemName;
    private String itemImageUrl;
    private String itemGrade;

    public static GachaResponse of(Item item, String itemsImageApiEndpoint) {
        return new GachaResponse(
                item.getViewName(),
                itemsImageApiEndpoint + item.getImageFileName(),
                item.getItemGrade().getViewName()
        );
    }
}
