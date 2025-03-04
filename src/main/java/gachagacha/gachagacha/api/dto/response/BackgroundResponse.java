package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.user.Background;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
