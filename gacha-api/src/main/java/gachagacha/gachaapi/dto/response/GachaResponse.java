package gachagacha.gachaapi.dto.response;

import gachagacha.domain.item.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
