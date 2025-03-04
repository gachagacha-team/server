package gachagacha.gachagacha.domain.minihome;

import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.minihome.MinihomeReader;
import gachagacha.gachagacha.domain.minihome.MinihomeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MinihomeService {

    private final MinihomeReader minihomeReader;
    private final MinihomeUpdater minihomeUpdater;

    public Minihome readMinihome(User user) {
        Minihome minihome = minihomeReader.findByUser(user);
        minihome.visit();
        return minihome;
    }

    @Transactional
    public Minihome visitMinihome(User minihomeUser) {
        Minihome minihome = minihomeReader.findByUser(minihomeUser);
        minihome.visit();
        minihomeUpdater.update(minihome);
        return minihome;
    }

    public Slice<Minihome> explore(Pageable pageable) {
        return minihomeReader.findAll(pageable);
    }

    public Slice<Minihome> exploreByScore(Pageable pageable) {
        return minihomeReader.findAllByScore(pageable);
    }
}
