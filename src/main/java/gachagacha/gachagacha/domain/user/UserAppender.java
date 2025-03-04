package gachagacha.gachagacha.domain.user;

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
