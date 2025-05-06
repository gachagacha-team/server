package gachagacha.db.meta;

import gachagacha.domain.meta.MinihomeMeta;
import gachagacha.domain.meta.MinihomeMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MinihomeMetaEntityRepository implements MinihomeMetaRepository {

    private final MinihomeMetaJpaRepository minihomeMetaJpaRepository;

    @Override
    @Async
    @Transactional
    public void increaseLikeCount(Long minihomeId) {
        minihomeMetaJpaRepository.increaseLikeCount(minihomeId);
    }

    @Override
    @Async
    @Transactional
    public void decreaseLikeCount(Long minihomeId) {
        minihomeMetaJpaRepository.decreaseLikeCount(minihomeId);
    }

    @Override
    public Long save(MinihomeMeta minihomeMeta) {
        MinihomeMetaEntity savedEntity = minihomeMetaJpaRepository.save(MinihomeMetaEntity.fromMinihomeMeta(minihomeMeta));
        return savedEntity.getId();
    }
}
