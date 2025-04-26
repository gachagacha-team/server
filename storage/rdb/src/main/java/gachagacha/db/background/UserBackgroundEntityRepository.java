package gachagacha.db.background;

import gachagacha.domain.user.Background;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserBackgroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBackgroundEntityRepository implements UserBackgroundRepository {

    private final UserBackgroundJpaRepository userBackgroundJpaRepository;

    @Override
    public List<Background> findByUser(User user) {
        return userBackgroundJpaRepository.findByUserId(user.getId()).stream()
                .map(userBackgroundEntity -> userBackgroundEntity.getBackground())
                .toList();
    }

    @Override
    public void saveBasicBackgrounds(long userId) {
        userBackgroundJpaRepository.save(new UserBackgroundEntity(null, Background.WHITE, userId));
        userBackgroundJpaRepository.save(new UserBackgroundEntity(null, Background.SKYBLUE, userId));
        userBackgroundJpaRepository.save(new UserBackgroundEntity(null, Background.CLOUD_GROUND, userId));
    }
}
