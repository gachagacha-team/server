package gachagacha.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum
ErrorCode {

    SERVICE_UNAVAILABLE(001, HttpStatus.SERVICE_UNAVAILABLE, "서비스 문제로, 잠시후 다시 시도해주세요."),

    // JWT
    REQUIRED_JWT(101, HttpStatus.UNAUTHORIZED, "토큰은 필수입니다."),
    EXPIRED_JWT(102, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_JWT(103, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(104, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // Bad Request
    INVALID_SOCIAL_TYPE(201, HttpStatus.BAD_REQUEST, "잘못된 socialType 입니다."),
    INVALID_ITEM_ID(202, HttpStatus.BAD_REQUEST, "잘못된 아이템 id 입니다."),
    INVALID_BACKGROUND_ID(202, HttpStatus.BAD_REQUEST, "잘못된 배경 id 입니다."),
    INVALID_TRADE_STATUS(203, HttpStatus.BAD_REQUEST, "잘못된 거래 상태 입니다."),
    INVALID_ITEM_GRADE(204, HttpStatus.BAD_REQUEST, "잘못된 아이템 등급입니다."),
    INVALID_PROFILE_ID(205, HttpStatus.BAD_REQUEST, "잘못된 프로필 id 입니다."),

    // Not Found
    NOT_FOUND_USER(301, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_ITEM(302, HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."),
    NOT_FOUND_MINIHOME(303, HttpStatus.NOT_FOUND, "미니홈을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(304, HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    NOT_FOUND_GUESTBOOK(305, HttpStatus.NOT_FOUND, "방명록을 찾을 수 없습니다."),
    NOT_FOUND_FOLLOW(306, HttpStatus.NOT_FOUND, "팔로우 내역을 찾을 수 없습니다."),
    NOT_FOUND_LOTTO(307, HttpStatus.NOT_FOUND, "복권을 찾을 수 없습니다."),
    NOT_FOUND_NOTIFICATION(308, HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),
    NOT_FOUND_MINIHOME_META(308, HttpStatus.NOT_FOUND, "미니홈 메타 데이터를 찾을 수 없습니다."),

    // Business Error
    DUPLICATED_USER_REGISTRATION(401, HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    DUPLICATED_NICKNAME(402, HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    REQUIRED_PROFILE_IMAGE(403, HttpStatus.BAD_REQUEST, "프로필 이미지는 필수입니다."),
    ALREADY_ATTEND(404, HttpStatus.FORBIDDEN, "이미 출석체크를 했습니다."),
    INSUFFICIENT_PRODUCT(405, HttpStatus.BAD_REQUEST, "수량이 부족해 구매가 불가합니다."),
    INSUFFICIENT_COIN(406, HttpStatus.BAD_REQUEST, "코인이 부족합니다."),
    CANNOT_EDIT_COMPLETED_TRADE(407, HttpStatus.BAD_REQUEST, "판매 완료된 거래는 수정이 불가합니다."),
    CANNOT_SELF_FOLLOW(408, HttpStatus.BAD_REQUEST, "자기 자신에게는 팔로우가 불가합니다."),
    ALREADY_FOLLOWING(409, HttpStatus.BAD_REQUEST, "이미 팔로우된 상태입니다."),
    CANNOT_REGISTER_TRADE(410, HttpStatus.BAD_REQUEST, "미니홈 꾸미기에 사용중인 아이템은 판매가 불가합니다."),
    ALREADY_USED_LOTTO(411, HttpStatus.BAD_REQUEST, "이미 사용한 복권입니다.")
    ;

    private final long code;
    private final HttpStatus httpStatus;
    private final String message;
}
