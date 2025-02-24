package gachagacha.gachagacha.implementation.minihome;

import gachagacha.gachagacha.domain.Minihome;
import gachagacha.gachagacha.repository.MinihomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinihomeRemover {

    private final MinihomeRepository minihomeRepository;

    public void delete(Minihome minihome) {
        minihomeRepository.delete(minihome.toMinihomeEntity());
    }
}
