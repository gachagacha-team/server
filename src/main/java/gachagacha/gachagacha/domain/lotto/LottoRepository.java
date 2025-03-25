package gachagacha.gachagacha.domain.lotto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoRepository extends JpaRepository<LottoEntity, Long> {

    List<LottoEntity> findByUserIdAndUsed(long userId, boolean used);
}
