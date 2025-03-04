package gachagacha.gachagacha.domain.user;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER))
                .toUser();
    }

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER))
                .toUser();
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public Optional<User> findBySocialTypeAndLoginId(SocialType socialType, long loginId) {
        return userRepository.findBySocialTypeAndLoginId(socialType, loginId)
                .map(userEntity -> userEntity.toUser());
    }

    public boolean existsBySocialTypeAndLoginId(SocialType socialType, long loginId) {
        return userRepository.findBySocialTypeAndLoginId(socialType, loginId).isPresent();
    }
}
