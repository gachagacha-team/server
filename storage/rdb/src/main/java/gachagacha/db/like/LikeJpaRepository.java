package gachagacha.db.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByMinihomeIdAndAndUserId(Long minihomeId, Long userId);
}
