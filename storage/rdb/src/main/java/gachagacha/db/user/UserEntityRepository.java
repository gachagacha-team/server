package gachagacha.db.user;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.user.SocialType;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserEntityRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findBySocialTypeAndLoginId(SocialType socialType, Long loginId) {
        return userJpaRepository.findBySocialTypeAndLoginId(socialType, loginId)
                .map(userEntity -> userEntity.toUser());
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
                .map(userEntity -> userEntity.toUser());
    }

    @Override
    public Optional<User> findById(long userId) {
        return userJpaRepository.findById(userId)
                .map(userEntity -> userEntity.toUser());
    }

    @Override
    public Long save(User user) {
        UserEntity userEntity = userJpaRepository.save(UserEntity.fromUser(user));
        return userEntity.getId();
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(UserEntity.fromUser(user));
    }

    @Override
    public void update(User user) {
        UserEntity userEntity = userJpaRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        userEntity.updateFromUser(user);
    }

    @Override
    public boolean existsBySocialTypeAndLoginId(SocialType socialType, long loginId) {
        return userJpaRepository.findBySocialTypeAndLoginId(socialType, loginId)
                .isPresent();
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
                .isPresent();
    }

    @Override
    public Slice<Long> findUserIdsOrderByScoreDescAndUserIdDesc(Pageable pageable, Integer score, Long userId) {
        return userJpaRepository.findUserIdsOrderByScoreDescAndUserIdDesc(pageable, score, userId);
    }
}
