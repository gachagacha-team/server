package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Item {

    PINK_DONUT(1, "pink_donut", Grade.A, "/items/pink_donut.png", 0, 0),
    PANCAKE(2, "pancake", Grade.A, "/items/pancake.png", 0, 0),
    CUPCAKE(3, "cupcake", Grade.A, "/items/cupcake.png", 0, 0),
    CROISSANT(4, "croissant", Grade.B, "/items/croissant.png", 0, 0),
    BLACK_CAT(5, "black_cat", Grade.C, "/items/black_cat.png", 0, 0),
    GRAY_WHITE_CAT(6, "gray_white_cat", Grade.C, "/items/gray_white_cat.png", 0, 0),
    GRAY_ORANGE_CAT(7, "gray_orange_cat", Grade.D, "/items/gray_orange_cat.png", 0, 0),
    YELLOW_CAT(8, "yellow_cat", Grade.D, "/items/yellow_cat.png", 0, 0),
    BUNNIES(9, "bunnies", Grade.A, "/items/bunnies.png", 0, 0),
    STRAWBERRY(10, "strawberry", Grade.B, "/items/strawberry.png", 0, 0),
    RED_BOY(11, "red_boy", Grade.C, "/items/red_boy.gif", 0, 0),
    PURPLE_BOY(12, "purple_boy", Grade.C, "/items/purple_boy.gif", 0, 0)
    ;

    private final long itemId;
    private final String viewName;
    private final Grade grade;
    private final String filePath;
    private int totalTradePrice;
    private int tradeCount;

    Item(long itemId, String viewName, Grade grade, String filePath, int totalTradePrice, int tradeCount) {
        this.itemId = itemId;
        this.viewName = viewName;
        this.grade = grade;
        this.filePath = filePath;
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

    public static Item findById(long id) {
        return Arrays.stream(Item.values())
                .filter(item -> item.itemId == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_ID));
    }
}
