package gachagacha.gachagacha.implementation.user;

import gachagacha.gachagacha.repository.UserRepository;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.UserEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserUpdator {

    private final UserRepository userRepository;

    @Transactional
    public void update(User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        userEntity.updateFromUser(user);
    }
}
