package gachagacha.gachagacha.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ItemGrade {

    A_PLUS(1, 10, "A+", 10),
    A(11, 25, "A", 8),
    B(26, 45, "B", 6),
    C(46, 70, "C", 4),
    D(71, 100, "D", 2)
    ;

    private final int startRange;
    private final int endRange;
    private final String viewName;
    private final int score;
    
    public static ItemGrade getItemGrade(int randomNumber) {
        return Arrays.stream(ItemGrade.values())
                .filter(itemGrade -> itemGrade.isInRange(randomNumber))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 랜덤번호입니다."));
    }

    private boolean isInRange(int randomNumber) {
        return randomNumber >= startRange && randomNumber <= endRange;
    }
}
