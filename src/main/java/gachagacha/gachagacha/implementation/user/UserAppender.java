package gachagacha.gachagacha.implementation.user;

import gachagacha.gachagacha.repository.UserRepository;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppender {

    private final UserRepository userRepository;

    public long save(User user) {
        UserEntity savedUserEntity = userRepository.save(user.toUserEntity());
        return savedUserEntity.getId();
    }
}
