package gachagacha.gachaapi.dto.response;

import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.item.Item;
import gachagacha.domain.user.Background;
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

    public static ReadDecorationResponse of (Decoration decoration, Background background, String itemsImageApiEndpoint, String backgroundsImageApiEndpoint) {
        List<DecorationItemResponse> decorationItemResponses = new ArrayList<>();
        if (decoration.getItems() != null) {
            decorationItemResponses = decoration.getItems().stream()
                    .map(decorationItem -> {
                        Item item = Item.findById(decorationItem.getItemId());
                        return new ReadDecorationResponse.DecorationItemResponse(
                                decorationItem.getUserItemId(),
                                decorationItem.getItemId(),
                                decorationItem.getX(),
                                decorationItem.getY(),
                                itemsImageApiEndpoint + item.getImageFileName()
                        );
                    })
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
        private long subId;
        private long itemId;
        private int x;
        private int y;
        private String imageUrl;
    }
}
