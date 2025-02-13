package gachagacha.gachagacha.exception;

import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.exception.customException.CustomJwtException;
import gachagacha.gachagacha.exception.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity handleBusinessException(BusinessException e) {
        log.info("handle BusinessException, message = {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ExceptionResponse(errorCode.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity handleJwtException(CustomJwtException e) {
        log.info("handle CustomJwtException, message = {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ExceptionResponse(errorCode.getMessage()));
    }
}
