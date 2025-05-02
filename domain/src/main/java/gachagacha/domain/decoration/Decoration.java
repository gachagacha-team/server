package gachagacha.domain.decoration;

import gachagacha.domain.user.Background;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Decoration {

    private Long userId;
    private Background background;
    private List<DecorationItem> decorationItems = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    public static class DecorationItem {

        private Long userItemId;
        private Long itemId;
        private int x;
        private int y;
    }
}
