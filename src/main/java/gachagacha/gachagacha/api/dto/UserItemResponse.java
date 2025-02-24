package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
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
