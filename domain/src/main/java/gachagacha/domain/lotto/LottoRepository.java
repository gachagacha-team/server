package gachagacha.domain.lotto;

import gachagacha.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LottoRepository {

    Long save(Lotto lotto);

    List<Lotto> findByUserAndUsed(User user, boolean used);

    Optional<Lotto> findById(long lottoId);

    void update(Lotto lotto);
}
