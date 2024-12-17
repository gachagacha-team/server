package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ItemType {

    CHICK_1(1, "CHICK", 1, "/items/chick_1.png"),
    CHICK_2(2, "CHICK", 2, "/items/chick_2.gif"),
    CHICK_3(3, "CHICK", 3, "/items/chick_3.gif"),
    SLIME_1(4, "SLIME", 1, "/items/slime_1.png"),
    SLIME_2(5, "SLIME", 2, "/items/slime_2.gif"),
    SLIME_3(6, "SLIME", 3, "/items/slime_3.gif"),
    HAMSTER_1(7, "HAMSTER", 1, "/items/hamster_1.png"),
    HAMSTER_2(8, "HAMSTER", 2, "/items/hamster_2.gif"),
    HAMSTER_3(9, "HAMSTER", 3, "/items/hamster_3.gif")
    ;

    private final int id;
    private final String itemName;
    private final int level;
    private final String filePath;

    public static ItemType findById(int id) {
        return Arrays.stream(ItemType.values())
                .filter(itemType -> itemType.id == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_TYPE_ID));
    }
}
