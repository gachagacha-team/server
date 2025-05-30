package gachagacha.domain.item;

import gachagacha.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository {

    Long save(UserItem userItem);

    List<UserItem> findByUserId(long userId);

    List<UserItem> findByUserAndItem(User user, Item item);

    void deleteAllByUserId(long userId);

    List<UserItem> findByUser(User user);

    Optional<UserItem> findById(long userItemId);

    void delete(UserItem userItem);
}
