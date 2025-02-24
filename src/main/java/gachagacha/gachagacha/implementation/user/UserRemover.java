package gachagacha.gachagacha.implementation.user;

import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.repository.UserRepository;
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
