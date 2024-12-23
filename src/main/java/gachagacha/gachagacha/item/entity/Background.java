package gachagacha.gachagacha.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Background {

    WHITE(1, "/backgrounds/white.png"),
    SKYBLUE(2, "/backgrounds/skyblue.png"),
    CLOUD_GROUND(3, "/backgrounds/cloud_ground.png")
    ;

    private final long id;
    private final String filePath;
}
