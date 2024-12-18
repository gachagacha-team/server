package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Item {
    CHICK_1(ItemType.CHARACTER ,1, 1, "/characters/chick_1.png", "chick_1", 0, 0),
    CHICK_2(ItemType.CHARACTER, 2, 2, "/characters/chick_2.gif", "chick_2", 0, 0),
    CHICK_3(ItemType.CHARACTER, 3, 3, "/characters/chick_3.gif", "chick_3", 0, 0),
    SLIME_1(ItemType.CHARACTER, 4, 1, "/characters/slime_1.png", "slime_1", 0, 0),
    SLIME_2(ItemType.CHARACTER, 5, 2, "/characters/slime_2.gif", "slime_2", 0, 0),
    SLIME_3(ItemType.CHARACTER, 6, 3, "/characters/slime_3.gif", "slime_3", 0, 0),
    HAMSTER_1(ItemType.CHARACTER, 7, 1, "/characters/hamster_1.png", "hamster_1", 0, 0),
    HAMSTER_2(ItemType.CHARACTER, 8, 2, "/characters/hamster_2.gif", "hamster_2", 0, 0),
    HAMSTER_3(ItemType.CHARACTER, 9, 3, "/characters/hamster_3.gif", "hamster_3", 0, 0),

    WHITE(ItemType.BACKGROUND, 101, 1, "/backgrounds/white.png", "white", 0, 0),
    SKYBLUE(ItemType.BACKGROUND, 102, 1, "/backgrounds/skyblue.png", "skyblue", 0, 0),
    BLUE_YELLOW(ItemType.BACKGROUND, 103, 1, "/backgrounds/blue_yellow.png", "blue_yellow", 0, 0),
    CLOUD_GROUND(ItemType.BACKGROUND, 104, 1, "/backgrounds/cloud_ground.png", "cloud_ground", 0, 0)
    ;

    private final ItemType itemType;
    private final int itemId;
    private final int level;
    private final String filePath;
    private final String viewName;
    private int totalTradePrice;
    private int tradeCount;

    Item(ItemType itemType, int itemId, int level, String filePath, String viewName, int totalTradePrice, int tradeCount) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.level = level;
        this.filePath = filePath;
        this.viewName = viewName;
        this.totalTradePrice = 0;
        this.tradeCount = 0;
    }

    public void addTrade(int price) {
        tradeCount++;
        totalTradePrice += price;
    }

    public int getAveragePrice() {
        return totalTradePrice / tradeCount;
    }

    public static Item findById(int id) {
        return Arrays.stream(Item.values())
                .filter(item -> item.itemId == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_ID));
    }
}
