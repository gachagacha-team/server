package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BackgroundType {

    WHITE(1, "/backgrounds/white.png"),
    SKYBLUE(2, "/backgrounds/skyblue.png"),
    BLUE_YELLOW(3, "/backgrounds/blue_yellow.png"),
    CLOUD_GROUND(4, "/backgrounds/cloud_ground.png")
    ;

    private final int id;
    private final String filePath;

    public static BackgroundType findById(int id) {
        return Arrays.stream(BackgroundType.values())
                .filter(backgroundType -> backgroundType.id == id)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_BACKGROUND_TYPE_ID));
    }
}
