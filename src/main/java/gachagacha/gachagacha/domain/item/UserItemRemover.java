package gachagacha.gachagacha.domain.item;

import gachagacha.gachagacha.domain.user.User;
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
