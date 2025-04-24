package gachagacha.domain.decoration;

import gachagacha.domain.item.UserItem;
import gachagacha.domain.user.User;

public interface DecorationRepository {

    void save(Decoration decoration, long userId);

    Decoration read(User user);

    boolean isUsedInMinihomeDecoration(UserItem userItem, User user);
}
