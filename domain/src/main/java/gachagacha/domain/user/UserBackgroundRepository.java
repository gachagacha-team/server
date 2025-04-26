package gachagacha.domain.user;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBackgroundRepository {
    List<Background> findByUser(User user);

    void saveBasicBackgrounds(long userId);
}
