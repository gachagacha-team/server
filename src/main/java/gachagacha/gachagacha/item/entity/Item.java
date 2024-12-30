package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
@RequiredArgsConstructor
public enum Item {
    // S
    RAINBOW_KIRBY(101, ItemGrade.S, "rainbow kirby", "rainbow_kirby.gif"),
    BUNNIES(102, ItemGrade.S, "bunnies", "bunnies.png"),
    MARIO(103, ItemGrade.S, "mario", "mario.gif"),

    // A
    MARIO_FLOWER(201, ItemGrade.A, "mario flower", "mario_flower.png"),
    PINK_DONUT(202, ItemGrade.A, "pink donut", "pink_donut.png"),
    PANCAKE(203, ItemGrade.A, "pancake", "pancake.png"),
    CUPCAKE(204, ItemGrade.A, "cupcake", "cupcake.png"),
    CROISSANT(205, ItemGrade.A, "croissant", "croissant.png"),

    // B
    MARIO_STAR(301, ItemGrade.B, "mario star", "mario_star.png"),
    MARIO_BLOCK(302, ItemGrade.B, "mario block", "mario_block.png"),
    STRAWBERRY(303, ItemGrade.B, "strawberry", "strawberry.png"),

    // C
    RED_BOY(401, ItemGrade.C, "red boy", "red_boy.gif"),
    PURPLE_BOY(402, ItemGrade.C, "purple boy", "purple_boy.gif"),

    // D
    RED_HARIBO(403, ItemGrade.D, "red haribo", "red_haribo.png"),
    ORANGE_HARIBO(404, ItemGrade.D, "orange haribo", "orange_haribo.png"),
    YELLOW_HARIBO(405, ItemGrade.D, "yellow haribo", "yellow_haribo.png"),
    GREEN_HARIBO(406, ItemGrade.D, "green haribo", "green_haribo.png"),
    BLUE_HARIBO(407, ItemGrade.D, "blue haribo", "blue_haribo.png"),
    PINK_HARIBO(408, ItemGrade.D, "pink haribo", "pink_haribo.png"),

    BLACK_CAT(501, ItemGrade.D, "black cat", "black_cat.png"),
    GRAY_WHITE_CAT(502, ItemGrade.D, "gray white cat", "gray_white_cat.png"),
    GRAY_ORANGE_CAT(503, ItemGrade.D, "gray orange cat", "gray_orange_cat.png"),
    YELLOW_CAT(504, ItemGrade.D, "yellow cat", "yellow_cat.png")
    ;

    private final long itemId;
    private final ItemGrade itemGrade;
    private final String viewName;
    private final String imageFileName;

    public static Item gacha(ItemGrade itemGrade) {
        List<Item> items = Arrays.stream(Item.values())
                .filter(item -> item.itemGrade == itemGrade)
                .toList();
        return items.get(new Random().nextInt(items.size()));
    }

    public static List<Item> getItemsByGrade(ItemGrade itemGrade) {
        return Arrays.stream(Item.values())
                .filter(item -> item.getItemGrade() == itemGrade)
                .toList();
    }

    public static Item findById(long id) {
        return Arrays.stream(Item.values())
                .filter(item -> item.itemId == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_ID));
    }
}
