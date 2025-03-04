package gachagacha.gachagacha.domain.minihome;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinihomeUpdater {

    private final MinihomeRepository minihomeRepository;

    public long update(Minihome minihome) {
        MinihomeEntity minihomeEntity = minihomeRepository.findById(minihome.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
        minihomeEntity.updateFromMinihome(minihome);
        return minihomeEntity.getId();
    }
}
