package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class UserItemsResponse {

    private long itemId;
    private String imageUrl;
    private String itemGrade;
    private int itemCnt;
    private List<Long> userItemIds;
    private String itemName;

    public static UserItemsResponse of(Item item, List<UserItem> userItems, String itemsImageApiEndpoint) {
        List<Long> userItemIds = userItems.stream()
                .map(userItem -> userItem.getId())
                .toList();
        return new UserItemsResponse(
                item.getItemId(),
                itemsImageApiEndpoint + item.getImageFileName(),
                item.getItemGrade().getViewName(),
                userItemIds.size(),
                userItemIds,
                item.getViewName()
        );
    }
}
