package gachagacha.db.item;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.item.UserItemRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserItemEntityRepository implements UserItemRepository {

    private final UserItemJpaRepository userItemJpaRepository;

    @Override
    public Long save(UserItem userItem) {
        UserItemEntity userItemEntity = userItemJpaRepository.save(UserItemEntity.fromUserItem(userItem));
        return userItemEntity.getId();
    }

    @Override
    public List<UserItem> findByUserId(long userId) {
        return userItemJpaRepository.findByUserId(userId).stream()
                .map(userItemEntity -> userItemEntity.toUserItem())
                .toList();
    }

    @Override
    public List<UserItem> findByUserAndItem(User user, Item item) {
        return userItemJpaRepository.findByUserIdAndItem(user.getId(), item)
                .stream()
                .map(userItemEntity -> userItemEntity.toUserItem())
                .toList();
    }

    @Override
    public void deleteAllByUserId(long userId) {
        userItemJpaRepository.deleteAllByUserId(userId);
    }

    @Override
    public List<UserItem> findByUser(User user) {
        return userItemJpaRepository.findByUserId(user.getId())
                .stream()
                .map(userItemEntity -> userItemEntity.toUserItem())
                .toList();
    }

    @Override
    public Optional<UserItem> findById(long userItemId) {
        return userItemJpaRepository.findById(userItemId)
                .map(userItemEntity -> userItemEntity.toUserItem());
    }

    @Override
    public void delete(UserItem userItem) {
        UserItemEntity userItemEntity = userItemJpaRepository.findById(userItem.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        userItemJpaRepository.delete(userItemEntity);
    }
}
