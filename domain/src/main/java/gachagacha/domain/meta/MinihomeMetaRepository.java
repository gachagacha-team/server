package gachagacha.domain.meta;

import org.springframework.stereotype.Repository;

@Repository
public interface MinihomeMetaRepository {

    void increaseLikeCount(Long minihomeId);

    void decreaseLikeCount(Long minihomeId);

    Long save(MinihomeMeta minihomeMeta);
}
