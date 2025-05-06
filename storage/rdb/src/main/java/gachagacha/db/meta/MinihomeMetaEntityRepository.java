package gachagacha.db.meta;

import gachagacha.domain.meta.MinihomeMeta;
import gachagacha.domain.meta.MinihomeMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    public Optional<MinihomeMeta> findByMinihomeId(Long minihomeId) {
        return minihomeMetaJpaRepository.findByMinihomeId(minihomeId)
                .map(minihomeMetaEntity -> minihomeMetaEntity.toMinihomeMeta());
    }

    @Override
    public Slice<MinihomeMeta> findAllBy(Pageable pageable) {
        return minihomeMetaJpaRepository.findAll(pageable)
                .map(minihomeMetaEntity -> minihomeMetaEntity.toMinihomeMeta());
    }
}
