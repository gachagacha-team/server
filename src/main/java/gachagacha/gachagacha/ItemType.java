package gachagacha.gachagacha;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ItemType {

    CHICK(1, 2, 3),
    SLIME(4, 5, 6),
    HAMSTER(7, 8, 9)
    ;

    private final int level1Id;
    private final int level2Id;
    private final int level3Id;
}
