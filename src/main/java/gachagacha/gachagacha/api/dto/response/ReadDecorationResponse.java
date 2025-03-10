package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.decoration.Decoration;
import gachagacha.gachagacha.domain.user.Background;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadDecorationResponse {

    private DecorationBackgroundResponse background;
    private List<DecorationItemResponse> items;

    public static ReadDecorationResponse of (String backgroundsImageApiEndpoint, Decoration decoration, Background background) {
        List<DecorationItemResponse> decorationItemResponses = new ArrayList<>();
        if (decoration.getItems() != null) {
            decorationItemResponses = decoration.getItems().stream()
                    .map(itemPosition -> new DecorationItemResponse(itemPosition.getUserItemId(), itemPosition.getX(), itemPosition.getY()))
                    .toList();
        }
        DecorationBackgroundResponse decorationBackgroundResponse = new DecorationBackgroundResponse(background.getId(), backgroundsImageApiEndpoint + background.getImageFileName());
        return new ReadDecorationResponse(decorationBackgroundResponse, decorationItemResponses);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DecorationBackgroundResponse {
        private long backgroundId;
        private String imageUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DecorationItemResponse {
        private long userItemId;
        private int x;
        private int y;
    }
}
