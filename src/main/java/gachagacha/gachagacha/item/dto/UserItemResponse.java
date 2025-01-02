package gachagacha.gachagacha.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserItemResponse {

    private long itemId;
    private String imageUrl;
    private String itemName;
    private String itemGrade;
    private List<Long> userItemIds;
    private int itemCnt;
    private boolean hasItem;
}
