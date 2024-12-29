package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ItemGrade {

    S(1, 10, "S", 100),
    A(11, 25, "A", 80),
    B(26, 45, "B", 60),
    C(46, 70, "C", 40),
    D(71, 100, "D", 20)
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

    public static ItemGrade findByViewName(String viewName) {
        System.out.println(viewName);
        return Arrays.stream(ItemGrade.values())
                .filter(itemGrade -> itemGrade.viewName.equals(viewName))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_GRADE));
    }

    private boolean isInRange(int randomNumber) {
        return randomNumber >= startRange && randomNumber <= endRange;
    }
}
