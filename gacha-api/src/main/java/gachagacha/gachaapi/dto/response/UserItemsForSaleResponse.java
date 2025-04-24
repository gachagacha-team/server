package gachagacha.gachaapi.dto.response;

import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class UserItemsForSaleResponse {

    private long itemId;
    private String imageUrl;
    private String itemGrade;
    private int itemCnt;
    private List<Long> userItemIds;
    private String itemName;
    private int price;
    private int stock;

    public static UserItemsForSaleResponse of(Item item, List<UserItem> userItems, int stock, String itemsImageApiEndpoint) {
        List<Long> userItemIds = userItems.stream()
                .map(userItem -> userItem.getId())
                .toList();
        return new UserItemsForSaleResponse(
                item.getItemId(),
                itemsImageApiEndpoint + item.getImageFileName(),
                item.getItemGrade().getViewName(),
                userItemIds.size(),
                userItemIds,
                item.getViewName(),
                item.getItemGrade().getPrice(),
                stock);
    }
}
