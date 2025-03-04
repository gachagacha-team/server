package gachagacha.gachagacha.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRemover {

    private final UserRepository userRepository;

    public void delete(User user) {
        userRepository.delete(user.toUserEntity());
    }
}
