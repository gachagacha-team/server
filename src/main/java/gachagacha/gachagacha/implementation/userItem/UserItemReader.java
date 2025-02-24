package gachagacha.gachagacha.implementation.userItem;

import gachagacha.gachagacha.repository.UserItemRepository;
import gachagacha.gachagacha.domain.Item;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.domain.UserItem;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserItemReader {

private final UserItemRepository userItemRepository;

    public UserItem findById(long userItemId) {
        return userItemRepository.findById(userItemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM))
                .toUserItem();
    }

    public List<UserItem> findAllByUser(User user) {
        return userItemRepository.findByUserId(user.getId())
                .stream()
                .map(userItemEntity -> userItemEntity.toUserItem())
                .toList();
    }

    public Page<UserItem> findAllByUser(User user, Pageable pageable) {
        return userItemRepository.findByUserId(user.getId(), pageable)
                .map(userItemEntity -> userItemEntity.toUserItem());
    }

    public List<UserItem> findAllByUserAndItem(User user, Item item) {
        return userItemRepository.findByUserIdAndItem(user.getId(), item)
                .stream()
                .map(userItemEntity -> userItemEntity.toUserItem())
                .toList();
    }
}
