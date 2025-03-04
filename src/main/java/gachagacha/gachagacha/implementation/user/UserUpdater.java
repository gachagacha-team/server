package gachagacha.gachagacha.implementation.user;

import gachagacha.gachagacha.repository.UserRepository;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.UserEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdater {

    private final UserRepository userRepository;

    public void update(User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        userEntity.updateFromUser(user);
    }
}
