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

    public static Decoration of (Long userId, Background background, List<DecorationItem> decorationItems) {
        return new Decoration(userId, background, decorationItems);
    }
}
