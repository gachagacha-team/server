package gachagacha.domain.minihome;

import gachagacha.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinihomeRepository {

    Slice<Minihome> findAllBy(Pageable pageable);
    Optional<Minihome> findByUserId(long userId);

    Long save(Minihome minihome);

    Optional<Minihome> findByUser(User user);

    Long update(Minihome minihome);

    void delete(Minihome minihome);
}
