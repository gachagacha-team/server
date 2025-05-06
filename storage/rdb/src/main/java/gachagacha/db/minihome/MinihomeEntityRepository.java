package gachagacha.db.minihome;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MinihomeEntityRepository implements MinihomeRepository {

    private final MinihomeJpaRepository minihomeJpaRepository;

    @Override
    public Slice<Minihome> findAllBy(Pageable pageable) {
        return minihomeJpaRepository.findAllBy(pageable)
                .map(minihomeEntity -> minihomeEntity.toMinihome());
    }

    @Override
    public Optional<Minihome> findByUserId(long userId) {
        return minihomeJpaRepository.findByUserId(userId)
                .map(minihomeEntity -> minihomeEntity.toMinihome());
    }

    @Override
    public Long save(Minihome minihome) {
        MinihomeEntity minihomeEntity = minihomeJpaRepository.save(MinihomeEntity.fromMinihome(minihome));
        return minihomeEntity.getId();
    }

    @Override
    public Optional<Minihome> findByUser(User user) {
        return minihomeJpaRepository.findByUserId(user.getId())
                .map(minihomeEntity -> minihomeEntity.toMinihome());
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
    public Optional<Minihome> findById(Long minihomeId) {
        return minihomeJpaRepository.findById(minihomeId)
                .map(minihomeEntity -> minihomeEntity.toMinihome());
    }
}
