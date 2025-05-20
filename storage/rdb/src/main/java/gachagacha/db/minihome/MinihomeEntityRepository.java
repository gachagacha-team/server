package gachagacha.db.minihome;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class MinihomeEntityRepository implements MinihomeRepository {

    private final MinihomeJpaRepository minihomeJpaRepository;
    private final MinihomeMetaJpaRepository minihomeMetaJpaRepository;

    @Override
    public Minihome findByUserId(long userId) {
        MinihomeEntity minihomeEntity = minihomeJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
        MinihomeMetaEntity minihomeMetaEntity = minihomeMetaJpaRepository.findByMinihomeId(minihomeEntity.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME_META));
        return new Minihome(minihomeEntity.getId(), minihomeEntity.getUserId(), minihomeEntity.getTotalVisitorCnt(), minihomeMetaEntity.getLikeCount());
    }

    @Override
    public Long save(Minihome minihome) {
        MinihomeEntity minihomeEntity = minihomeJpaRepository.save(MinihomeEntity.fromMinihome(minihome));
        minihomeMetaJpaRepository.save(new MinihomeMetaEntity(null, minihomeEntity.getId(), minihome.getLikeCount()));
        return minihomeEntity.getId();
    }

    @Override
    public Long update(Minihome minihome) {
        MinihomeEntity minihomeEntity = minihomeJpaRepository.findById(minihome.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
        minihomeEntity.updateFromMinihome(minihome);
        return minihomeEntity.getId();
    }

    @Override
    public void delete(Minihome minihome) {
        minihomeJpaRepository.delete(MinihomeEntity.fromMinihome(minihome));
    }

    @Override
    public void increaseVisitorCount(Long minihomeId) {
        minihomeJpaRepository.increaseVisitorCount(minihomeId);
    }

    @Override
    public Minihome findById(Long minihomeId) {
        MinihomeEntity minihomeEntity = minihomeJpaRepository.findById(minihomeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
        MinihomeMetaEntity minihomeMetaEntity = minihomeMetaJpaRepository.findByMinihomeId(minihomeEntity.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME_META));
        return new Minihome(minihomeEntity.getId(), minihomeEntity.getUserId(), minihomeEntity.getTotalVisitorCnt(), minihomeMetaEntity.getLikeCount());
    }

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
    public Slice<Long> findMinihomeIdsOrderByCreatedAtDescAndMinihomeIdDesc(Pageable pageable, LocalDateTime createdAt, Long minihomeId) {
        return minihomeJpaRepository.findMinihomeIdsOrderByCreatedAtDescAndMinihomeIdDesc(pageable, createdAt, minihomeId);
    }

    @Override
    public Slice<Long> findMinihomeIdsOrderByTotalVisitorCntDescAndMinihomeIdDesc(Pageable pageable, Integer totalVisitorCnt, Long minihomeId) {
        return minihomeJpaRepository.findMinihomeIdsOrderByTotalVisitorCntDescAndMinihomeIdDesc(pageable, totalVisitorCnt, minihomeId);
    }

    @Override
    public Slice<Long> findMinihomeIdsOrderByLikeCountDescAndMinihomeIdDesc(Pageable pageable, long likeCount, long minihomeId) {
        return minihomeMetaJpaRepository.findMinihomeIdsOrderByLikeCountDescAndMinihomeIdDesc(pageable, likeCount, minihomeId);
    }
}
