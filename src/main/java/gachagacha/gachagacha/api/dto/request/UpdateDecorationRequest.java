package gachagacha.gachagacha.api.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UpdateDecorationRequest {

    private long backgroundId;
    private List<DecorationItemRequest> items;

    @Getter
    @ToString
    public static class DecorationItemRequest {
        private long itemId;
        private long subId;
        private int x;
        private int y;
    }
}
