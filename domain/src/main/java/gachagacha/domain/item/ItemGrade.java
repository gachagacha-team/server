package gachagacha.domain.item;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Random;

@Getter
@RequiredArgsConstructor
public enum ItemGrade {

    S(1, 10, "S", 100, 10000),
    A(11, 25, "A", 80, 8000),
    B(26, 45, "B", 60, 6000),
    C(46, 70, "C", 40, 4000),
    D(71, 100, "D", 20, 2000)
    ;

    private final int startRange;
    private final int endRange;
    private final String viewName;
    private final int score;
    private final int price;

    public static ItemGrade findByViewName(String viewName) {
        return Arrays.stream(ItemGrade.values())
                .filter(itemGrade -> itemGrade.viewName.equals(viewName))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ITEM_GRADE));
    }

    public static ItemGrade getRandomItemGrade() {
        int randomNumber = new Random().nextInt(100) + 1;
        return Arrays.stream(ItemGrade.values())
                .filter(itemGrade -> itemGrade.isInRange(randomNumber))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 랜덤번호입니다."));
    }

    private boolean isInRange(int randomNumber) {
        return randomNumber >= startRange && randomNumber <= endRange;
    }
}
