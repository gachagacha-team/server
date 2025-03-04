package gachagacha.gachagacha.domain.item;

import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.item.entity.UserItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserItem {

    private Long id;
    private Item item;
    private Long userId;

    public static UserItem of(User user, Item item) {
        return new UserItem(
                null,
                item,
                user.getId()
        );
    }

    public UserItemEntity toUserItemEntity() {
        return new UserItemEntity(
                id,
                item,
                userId
        );
    }
}
