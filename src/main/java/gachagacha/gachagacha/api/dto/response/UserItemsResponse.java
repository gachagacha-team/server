package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.item.UserItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserItemsResponse {

    private long itemId;
    private long subId;
    private String imageUrl;
    private String itemGrade;

    public static UserItemsResponse of(UserItem userItem, String itemsImageApiEndpoint) {
        return new UserItemsResponse(
                userItem.getItem().getItemId(),
                userItem.getId(),
                itemsImageApiEndpoint + userItem.getItem().getImageFileName(),
                userItem.getItem().getItemGrade().getViewName()
        );
    }
}
