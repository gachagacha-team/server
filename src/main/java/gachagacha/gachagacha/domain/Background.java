package gachagacha.gachagacha.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Background {

    WHITE(1, "white.png"),
    SKYBLUE(2, "skyblue.png"),
    CLOUD_GROUND(3, "cloud_ground.png")
    ;

    private final long id;
    private final String imageFileName;
}
