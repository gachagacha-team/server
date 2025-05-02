package gachagacha.db.lotto;

import gachagacha.domain.item.ItemGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoJpaRepository extends JpaRepository<LottoEntity, Long> {

    List<LottoEntity> findByUserIdAndUsed(long userId, boolean used);

    List<LottoEntity> findByUserIdAndItemGrade(Long userId, ItemGrade itemGrade);
}
