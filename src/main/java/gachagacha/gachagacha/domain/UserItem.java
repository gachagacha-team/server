package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.UserItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserItem {

    private long id;
    private Item item;
    private long userId;

    public static UserItem of(User user, Item item) {
        return new UserItem(
                0l,
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
