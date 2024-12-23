package gachagacha.gachagacha.trade.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {

    ON_SALE("판매 중"),
    COMPLETED("거래 완료"),
    CANCELLED("판매 취소")
    ;

    private final String viewName;

    public static TradeStatus findByViewName(String status) {
        return Arrays.stream(TradeStatus.values())
                .filter(tradeStatus -> tradeStatus.getViewName().equals(status))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TRADE_STATUS));
    }
}
