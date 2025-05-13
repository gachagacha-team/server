package gachagacha.domain.like;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository {

    Optional<Like> findByMinihomeIdAndAndUserId(Long minihomeId, Long userId);

    void save(Like like);

    void delete(Like like);
}
