package gachagacha.gachagacha.domain.minihome;

import gachagacha.gachagacha.domain.user.UserRepository;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinihomeReader {

    private final MinihomeRepository minihomeRepository;
    private final UserRepository userRepository;

    public Minihome findByUser(User user) {
        return minihomeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME))
                .toMinihome();
    }

    public Slice<Minihome> findAll(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable)
                .map(minihomeEntity -> minihomeEntity.toMinihome());
    }

    public Slice<Minihome> findAllByScore(Pageable pageable) {
        return userRepository.findAllBy(pageable)
                .map(userEntity -> {
                    MinihomeEntity minihomeEntity = minihomeRepository.findByUserId(userEntity.getId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
                    return minihomeEntity.toMinihome();
                });
    }
}
