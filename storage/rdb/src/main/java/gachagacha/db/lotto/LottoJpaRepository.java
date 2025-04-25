package gachagacha.db.lotto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoJpaRepository extends JpaRepository<LottoEntity, Long> {

    List<LottoEntity> findByUserIdAndUsed(long userId, boolean used);
}
