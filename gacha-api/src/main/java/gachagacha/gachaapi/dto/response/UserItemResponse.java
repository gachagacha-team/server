package gachagacha.gachaapi.dto.response;

import gachagacha.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserItemResponse {

    private long itemId;
    private String imageUrl;
    private String itemGrade;
    private int itemCnt;

    public static UserItemResponse of(Item item, int itemCnt, String itemsImageApiEndpoint) {
        return new UserItemResponse(
                item.getItemId(),
                itemsImageApiEndpoint + item.getImageFileName(),
                item.getItemGrade().getViewName(),
                itemCnt);
    }
}
