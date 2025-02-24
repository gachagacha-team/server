package gachagacha.gachagacha.support.exception.customException;

import gachagacha.gachagacha.support.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
