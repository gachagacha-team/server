package gachagacha.gachagacha.implementation.userItem;

import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.repository.UserItemRepository;
import gachagacha.gachagacha.domain.UserItem;
import gachagacha.gachagacha.entity.UserItemEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserItemRemover {

    private final UserItemRepository userItemRepository;

    public void delete(UserItem userItem) {
        UserItemEntity userItemEntity = userItemRepository.findById(userItem.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        userItemRepository.delete(userItemEntity);
    }

    public void deleteByUser(User user) {
        userItemRepository.deleteAllByUserId(user.getId());
    }
}
