package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum LoginType {

    GITHUB("github"),
    KAKAO("kakao");

    private final String name;

    public static LoginType find(String name) {
        return Arrays.stream(LoginType.values())
                .filter(loginType -> loginType.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_LOGIN_TYPE));
    }
}
