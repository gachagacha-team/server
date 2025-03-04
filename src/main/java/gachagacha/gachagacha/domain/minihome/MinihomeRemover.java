package gachagacha.gachagacha.domain.minihome;

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
