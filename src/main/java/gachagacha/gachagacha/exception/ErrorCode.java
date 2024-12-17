package gachagacha.gachagacha.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 loginType 입니다."),
    DUPLICATED_USER_REGISTRATION(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),

    // jwt
    EXPIRED_JWT(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    INVALID_JWT(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    REQUIRED_JWT(HttpStatus.BAD_REQUEST, "토큰은 필수입니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    INVALID_ITEM_ID(HttpStatus.BAD_REQUEST, "잘못된 아이템 id 입니다."),

    ALREADY_ATTEND(HttpStatus.FORBIDDEN, "이미 출석체크를 했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
