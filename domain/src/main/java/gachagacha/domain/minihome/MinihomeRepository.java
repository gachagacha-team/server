package gachagacha.domain.minihome;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MinihomeRepository {

    Minihome findByUserId(long userId);

    Long save(Minihome minihome);

    Long update(Minihome minihome);

    void delete(Minihome minihome);

    void increaseVisitorCount(Long minihomeId);

    Minihome findById(Long minihomeId);

    void increaseLikeCount(Long minihomeId);

    void decreaseLikeCount(Long minihomeId);

    Slice<Long> findMinihomeIdsOrderByCreatedAtDescAndMinihomeIdDesc(Pageable pageable, LocalDateTime createdAt, Long minihomeId);

    Slice<Long> findMinihomeIdsOrderByTotalVisitorCntDescAndMinihomeIdDesc(Pageable pageable, Integer totalVisitorCnt, Long minihomeId);

    Slice<Long> findMinihomeIdsOrderByLikeCountDescAndMinihomeIdDesc(Pageable pageable, long likeCount, long minihomeId);
}
