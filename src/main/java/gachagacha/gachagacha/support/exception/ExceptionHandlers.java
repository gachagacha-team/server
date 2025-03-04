package gachagacha.gachagacha.support.exception;

import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.support.exception.customException.CustomJwtException;
import gachagacha.gachagacha.support.api_response.ApiResponse;
import gachagacha.gachagacha.support.api_response.ErrorResponse;
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
