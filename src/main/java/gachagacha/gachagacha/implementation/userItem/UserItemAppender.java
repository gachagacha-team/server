package gachagacha.gachagacha.implementation.userItem;

import gachagacha.gachagacha.repository.UserItemRepository;
import gachagacha.gachagacha.domain.UserItem;
import gachagacha.gachagacha.entity.UserItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserItemAppender {

    private final UserItemRepository userItemRepository;

    public long save(UserItem userItem) {
        UserItemEntity saveUserItemEntity = userItemRepository.save(userItem.toUserItemEntity());
        return saveUserItemEntity.getId();
    }

    public void addUserItem(UserItem userItem) {
        userItemRepository.save(userItem.toUserItemEntity());
    }
}
