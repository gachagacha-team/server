package gachagacha.domain.decoration;

import gachagacha.domain.user.Background;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Decoration {

    private Long userId;
    private Background background;
    private List<DecorationItem> decorationItems = new ArrayList<>();

    public static Decoration of(Long userId, Background background, List<DecorationItem> decorationItems) {
        return new Decoration(userId, background, decorationItems);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DecorationItem {

        private Long userItemId;
        private Long itemId;
        private int x;
        private int y;

        public static DecorationItem of(long userItemId, long itemId, int x, int y) {
            return new DecorationItem(userItemId, itemId, x, y);
        }
    }
}
