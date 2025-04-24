package gachagacha.domain.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Profile {

    BEAR(1),
    COW(2),
    GIRAFFE(3)
    ;

    private final long id;

    public static Profile findById(Long id) {
        return Arrays.stream(Profile.values())
                .filter(profile -> profile.id == id)
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PROFILE_ID));
    }
}
