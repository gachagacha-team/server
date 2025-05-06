package gachagacha.domain.meta;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinihomeMetaRepository {

    void increaseLikeCount(Long minihomeId);

    void decreaseLikeCount(Long minihomeId);

    Long save(MinihomeMeta minihomeMeta);

    Optional<MinihomeMeta> findByMinihomeId(Long minihomeId);

    Slice<MinihomeMeta> findAllBy(Pageable pageable);
}
