package gachagacha.gachagacha.product.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    ON_SALE("판매 중"),
    COMPLETED("판매 완료")
    ;

    private final String viewName;

    public static ProductStatus findByViewName(String status) {
        return Arrays.stream(ProductStatus.values())
                .filter(productStatus -> productStatus.getViewName().equals(status))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TRADE_STATUS));
    }
}
