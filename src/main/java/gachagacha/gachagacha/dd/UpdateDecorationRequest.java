package gachagacha.gachagacha.dd;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateDecorationRequest {

    private long backgroundId;
    private List<DecorationItemRequest> items;

    @Getter
    public static class DecorationItemRequest {
        private long itemId;
        private long userItemId;
        private int x;
        private int y;
    }
}
