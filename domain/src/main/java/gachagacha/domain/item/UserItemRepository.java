package gachagacha.domain.item;

import gachagacha.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository {

    Long save(UserItem userItem);
    List<UserItem> findByUserId(long userId);

    Page<UserItem> findByUserId(long userId, Pageable pageable);

    List<UserItem> findByUserAndItem(User user, Item item);

    void deleteAllByUserId(long userId);

    List<UserItem> findByUser(User user);


    Optional<UserItem> findById(long userItemId);

    void delete(UserItem userItem);
}
