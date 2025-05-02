package gachagacha.gachaapi.common;

import gachagacha.gachaapi.common.ApiResponse;
import gachagacha.gachaapi.common.ErrorResponse;
import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.common.exception.customException.CustomJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    public ApiResponse handleBusinessException(BusinessException e) {
        log.info("handle BusinessException, message = {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ApiResponse.error(new ErrorResponse(errorCode.getCode(), errorCode.getHttpStatus(), errorCode.getMessage()));
    }

    @ExceptionHandler
    public ApiResponse handleJwtException(CustomJwtException e) {
        log.info("handle CustomJwtException, message = {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ApiResponse.error(new ErrorResponse(errorCode.getCode(), errorCode.getHttpStatus(), errorCode.getMessage()));
    }
}
