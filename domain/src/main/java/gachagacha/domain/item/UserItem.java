package gachagacha.domain.item;

import gachagacha.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public boolean isOwnedBy(User user) {
        return user.getId() == userId;
    }
}
