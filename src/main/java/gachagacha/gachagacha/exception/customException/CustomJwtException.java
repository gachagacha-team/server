package gachagacha.gachagacha.exception.customException;

import gachagacha.gachagacha.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomJwtException extends RuntimeException {

    private final ErrorCode errorCode;
}
