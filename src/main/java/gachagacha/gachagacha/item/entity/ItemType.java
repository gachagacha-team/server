package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ItemType {

    CHICK_1(1, "CHICK", 1, "/items/chick_1.png", 0, 0),
    CHICK_2(2, "CHICK", 2, "/items/chick_2.gif", 0, 0),
    CHICK_3(3, "CHICK", 3, "/items/chick_3.gif", 0, 0),
    SLIME_1(4, "SLIME", 1, "/items/slime_1.png", 0, 0),
    SLIME_2(5, "SLIME", 2, "/items/slime_2.gif", 0, 0),
    SLIME_3(6, "SLIME", 3, "/items/slime_3.gif", 0, 0),
    HAMSTER_1(7, "HAMSTER", 1, "/items/hamster_1.png", 0, 0),
    HAMSTER_2(8, "HAMSTER", 2, "/items/hamster_2.gif", 0, 0),
    HAMSTER_3(9, "HAMSTER", 3, "/items/hamster_3.gif", 0, 0)
    ;

    private final int id;
    private final String itemName;
    private final int level;
    private final String filePath;
    private int totalTradePrice;
    private int tradeCount;

    ItemType(int id, String itemName, int level, String filePath, int totalTradePrice, int tradeCount) {
        this.id = id;
        this.itemName = itemName;
        this.level = level;
        this.filePath = filePath;
        this.totalTradePrice = totalTradePrice;
        this.tradeCount = tradeCount;
    }

    public void addTrade(int price) {
        tradeCount++;
        totalTradePrice += price;
    }

    public int getAveragePrice() {
        return totalTradePrice / tradeCount;
    }

    public static ItemType findById(int id) {
        return Arrays.stream(ItemType.values())
                .filter(itemType -> itemType.id == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_TYPE_ID));
    }
}
