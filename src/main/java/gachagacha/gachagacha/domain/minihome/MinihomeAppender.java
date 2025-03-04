package gachagacha.gachagacha.domain.minihome;

import gachagacha.gachagacha.domain.item.entity.MinihomeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinihomeAppender {

    private final MinihomeRepository minihomeRepository;

    public long save(Minihome minihome) {
        MinihomeEntity saveMinihomeEntity = minihomeRepository.save(minihome.toMinihomeEntity());
        return saveMinihomeEntity.getId();
    }
}
