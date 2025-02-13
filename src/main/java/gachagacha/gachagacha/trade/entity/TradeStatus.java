package gachagacha.gachagacha.trade.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {

    ON_SALE("판매 중"),
    COMPLETED("판매 완료")
    ;

    private final String viewName;
}
