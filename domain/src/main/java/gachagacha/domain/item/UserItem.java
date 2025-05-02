package gachagacha.domain.item;

import gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserItem {

    private Long id;
    private Item item;
    private Long userId;

    public boolean isOwnedBy(User user) {
        return user.getId() == userId;
    }
}
