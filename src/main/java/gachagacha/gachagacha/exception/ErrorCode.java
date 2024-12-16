package gachagacha.gachagacha.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 loginType 입니다."),
    DUPLICATED_USER_REGISTRATION(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
