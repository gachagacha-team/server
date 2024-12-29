package gachagacha.gachagacha.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserItemResponse {

    private long itemId;
    private String itemName;
    private String itemGrade;
    private boolean owned;
    private String imageUrl;
}
