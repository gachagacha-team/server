package gachagacha.gachagacha.domain.decoration;

import gachagacha.gachagacha.domain.user.Background;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Decoration implements Serializable {

    private long backgroundId;
    private List<DecorationItem> items = new ArrayList<>();

    public static Decoration of(Background background, List<DecorationItem> decorationItems) {
        return new Decoration(background.getId(), decorationItems);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DecorationItem implements Serializable {
        private long itemId;
        private long userItemId;
        private int x;
        private int y;

        public static DecorationItem of(long itemId, long userItemId, int x, int y) {
            return new DecorationItem(itemId, userItemId, x, y);
        }
    }
}
