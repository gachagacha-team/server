package gachagacha.gachagacha.exception;

import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.exception.customException.CustomJwtException;
import gachagacha.gachagacha.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ExceptionResponse(errorCode.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity handleJwtException(CustomJwtException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(errorCode.getMessage()));
    }
}
