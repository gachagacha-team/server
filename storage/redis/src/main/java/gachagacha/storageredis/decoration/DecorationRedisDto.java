package gachagacha.storageredis.decoration;

import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.user.Background;
import gachagacha.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DecorationRedisDto implements Serializable {

    private long backgroundId;
    private List<DecorationItemDto> items = new ArrayList<>();

    public static DecorationRedisDto of(Background background, List<DecorationItemDto> decorationItemDtos) {
        return new DecorationRedisDto(background.getId(), decorationItemDtos);
    }

    public static DecorationRedisDto fromDecoration(Decoration decoration) {
        List<DecorationItemDto> itemDtos = decoration.getDecorationItems().stream()
                .map(decorationItem -> new DecorationItemDto(decorationItem.getItemId(), decorationItem.getUserItemId(), decorationItem.getX(), decorationItem.getY()))
                .toList();
        return new DecorationRedisDto(decoration.getBackground().getId(), itemDtos);
    }

    public Decoration toDecoration(User user) {
        List<Decoration.DecorationItem> items = this.items.stream()
                .map(decorationItemDto -> new Decoration.DecorationItem(decorationItemDto.getUserItemId(), decorationItemDto.getItemId(), decorationItemDto.getX(), decorationItemDto.getY()))
                .toList();
        return new Decoration(user.getId(), Background.findById(backgroundId), items);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DecorationItemDto implements Serializable {
        private long itemId;
        private long userItemId;
        private int x;
        private int y;

        public static DecorationItemDto of(long itemId, long userItemId, int x, int y) {
            return new DecorationItemDto(itemId, userItemId, x, y);
        }
    }
}
