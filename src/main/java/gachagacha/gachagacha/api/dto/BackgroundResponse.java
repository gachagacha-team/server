package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Background;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BackgroundResponse {

    private long backgroundId;
    private String imageUrl;

    public static BackgroundResponse of(Background background, String backgroundsImageApiEndpoint) {
        return new BackgroundResponse(
                background.getId(),
                backgroundsImageApiEndpoint + background.getImageFileName()
        );
    }
}
