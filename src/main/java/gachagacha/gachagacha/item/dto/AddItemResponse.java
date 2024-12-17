package gachagacha.gachagacha.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemResponse {

    private long itemId;
    private int itemTypeId;
    private String nickname;
}
