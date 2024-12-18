package gachagacha.gachagacha.trade.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {

    ON_SALE("판매 중"),
    COMPLETED("거래 완료"),
    CANCELLED("판매 취소")
    ;

    private final String viewName;
}
