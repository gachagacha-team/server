package gachagacha.gachagacha.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum
ErrorCode {

    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "잘못된 socialType 입니다."),
    DUPLICATED_USER_REGISTRATION(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    REQUIRED_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "프로필 이미지는 필수입니다."),

    // jwt
    REQUIRED_JWT(HttpStatus.UNAUTHORIZED, "토큰은 필수입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 리프레시 토큰입니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."),
    NOT_FOUND_MINIHOME(HttpStatus.NOT_FOUND, "미니홈을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    NOT_FOUND_GUESTBOOK(HttpStatus.NOT_FOUND, "방명록을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    INVALID_ITEM_ID(HttpStatus.BAD_REQUEST, "잘못된 아이템 id 입니다."),
    INVALID_TRADE_STATUS(HttpStatus.BAD_REQUEST, "잘못된 거래 상태 입니다."),

    ALREADY_ATTEND(HttpStatus.FORBIDDEN, "이미 출석체크를 했습니다."),

    INSUFFICIENT_COIN(HttpStatus.BAD_REQUEST, "코인이 부족합니다."),
    CANNOT_EDIT_COMPLETED_TRADE(HttpStatus.BAD_REQUEST, "판매 완료된 거래는 수정이 불가합니다."),

    CANNOT_SELF_FOLLOW(HttpStatus.BAD_REQUEST, "자기 자신에게는 팔로우가 불가합니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우된 상태입니다."),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "팔로우 내역을 찾을 수 없습니다."),

    INVALID_ITEM_GRADE(HttpStatus.BAD_REQUEST, "잘못된 아이템 등급입니다."),

    INSUFFICIENT_PRODUCT(HttpStatus.BAD_REQUEST, "수량이 부족해 구매가 불가합니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
