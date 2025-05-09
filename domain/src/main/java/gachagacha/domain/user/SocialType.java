package gachagacha.domain.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SocialType {

    GITHUB("github"),
    KAKAO("kakao");

    private final String name;

    public static SocialType of(String name) {
        return Arrays.stream(SocialType.values())
                .filter(loginType -> loginType.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SOCIAL_TYPE));
    }
}
