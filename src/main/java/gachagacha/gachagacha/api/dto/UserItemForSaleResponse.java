package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserItemForSaleResponse {

    private long itemId;
    private String imageUrl;
    private String itemGrade;
    private int itemCnt;
    private String itemName;
    private int price;
    private int stock;

    public static UserItemForSaleResponse of(Item item, int itemCnt, int stock, String itemsImageApiEndpoint) {
        return new UserItemForSaleResponse(
                item.getItemId(),
                itemsImageApiEndpoint + item.getImageFileName(),
                item.getItemGrade().getViewName(),
                itemCnt,
                item.getViewName(),
                item.getItemGrade().getPrice(),
                stock);
    }
}
