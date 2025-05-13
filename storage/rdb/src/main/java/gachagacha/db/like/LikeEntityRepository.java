package gachagacha.db.like;

import gachagacha.domain.like.Like;
import gachagacha.domain.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeEntityRepository implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Optional<Like> findByMinihomeIdAndAndUserId(Long minihomeId, Long userId) {
        return likeJpaRepository.findByMinihomeIdAndAndUserId(minihomeId, userId)
                .map(likeEntity -> likeEntity.toLike());
    }

    @Override
    public void save(Like like) {
        likeJpaRepository.save(LikeEntity.fromLike(like));
    }

    @Override
    public void delete(Like like) {
        likeJpaRepository.delete(LikeEntity.fromLike(like));
    }
}
