package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
public enum Item {
    // A+
    RAINBOW_KIRBY(101, ItemGrade.A_PLUS, "rainbow kirby", "rainbow_kirby.gif", 0, 0),
    BUNNIES(102, ItemGrade.A_PLUS, "bunnies", "bunnies.png", 0, 0),
    MARIO(103, ItemGrade.A_PLUS, "mario", "mario.gif", 0, 0),

    // A
    MARIO_FLOWER(201, ItemGrade.A, "mario flower", "mario_flower.png", 0, 0),
    PINK_DONUT(202, ItemGrade.A, "pink donut", "pink_donut.png", 0, 0),
    PANCAKE(203, ItemGrade.A, "pancake", "pancake.png", 0, 0),
    CUPCAKE(204, ItemGrade.A, "cupcake", "cupcake.png", 0, 0),
    CROISSANT(205, ItemGrade.A, "croissant", "croissant.png", 0, 0),

    // B
    MARIO_STAR(301, ItemGrade.B, "mario star", "mario_star.png", 0, 0),
    MARIO_BLOCK(302, ItemGrade.B, "mario block", "mario_block.png", 0, 0),
    STRAWBERRY(303, ItemGrade.B, "strawberry", "strawberry.png", 0, 0),

    // C
    RED_BOY(401, ItemGrade.C, "red boy", "red_boy.gif", 0, 0),
    PURPLE_BOY(402, ItemGrade.C, "purple boy", "purple_boy.gif", 0, 0),
    RED_HARIBO(403, ItemGrade.C, "red haribo", "red_haribo.png", 0, 0),
    ORANGE_HARIBO(404, ItemGrade.C, "orange haribo", "orange_haribo.png", 0, 0),
    YELLOW_HARIBO(405, ItemGrade.C, "yellow haribo", "yellow_haribo.png", 0, 0),
    GREEN_HARIBO(406, ItemGrade.C, "green haribo", "green_haribo.png", 0, 0),
    BLUE_HARIBO(407, ItemGrade.C, "blue haribo", "blue_haribo.png", 0, 0),
    PINK_HARIBO(408, ItemGrade.C, "pink haribo", "pink_haribo.png", 0, 0),

    // D
    BLACK_CAT(501, ItemGrade.D, "black cat", "black_cat.png", 0, 0),
    GRAY_WHITE_CAT(502, ItemGrade.D, "gray white cat", "gray_white_cat.png", 0, 0),
    GRAY_ORANGE_CAT(503, ItemGrade.D, "gray orange cat", "gray_orange_cat.png", 0, 0),
    YELLOW_CAT(504, ItemGrade.D, "yellow cat", "yellow_cat.png", 0, 0)
    ;


    private final long itemId;
    private final ItemGrade itemGrade;
    private final String viewName;
    private final String imageFileName;
    private int totalTradePrice;
    private int tradeCount;

    Item(long itemId, ItemGrade itemGrade, String viewName, String imageFileName, int totalTradePrice, int tradeCount) {
        this.itemId = itemId;
        this.itemGrade = itemGrade;
        this.viewName = viewName;
        this.imageFileName = imageFileName;
        this.totalTradePrice = totalTradePrice;
        this.tradeCount = tradeCount;
    }

    public static Item gacha(ItemGrade itemGrade) {
        List<Item> items = Arrays.stream(Item.values())
                .filter(item -> item.itemGrade == itemGrade)
                .toList();
        return items.get(new Random().nextInt(items.size()));
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
