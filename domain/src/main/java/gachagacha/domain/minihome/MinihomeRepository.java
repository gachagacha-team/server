package gachagacha.domain.minihome;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface MinihomeRepository {

    Slice<Minihome> findAllBy(Pageable pageable);

    Minihome findByUserId(long userId);

    Long save(Minihome minihome);

    Long update(Minihome minihome);

    void delete(Minihome minihome);

    void increaseVisitorCount(Long minihomeId);

    Minihome findById(Long minihomeId);

    void increaseLikeCount(Long minihomeId);

    void decreaseLikeCount(Long minihomeId);

    Slice<Minihome> findAllByLikeCount(Pageable pageable);
}
