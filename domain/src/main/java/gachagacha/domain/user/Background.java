package gachagacha.domain.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Background {

    WHITE(1, "white.png"),
    SKYBLUE(2, "skyblue.png"),
    CLOUD_GROUND(3, "cloud_ground.png")
    ;

    private final long id;
    private final String imageFileName;

    public static Background findById(long id) {
        return Arrays.stream(Background.values())
                .filter(background -> background.id == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_BACKGROUND_ID));
    }
}
