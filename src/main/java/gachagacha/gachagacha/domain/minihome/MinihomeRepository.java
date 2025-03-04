package gachagacha.gachagacha.domain.minihome;

import gachagacha.gachagacha.domain.item.entity.MinihomeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinihomeRepository extends JpaRepository<MinihomeEntity, Long> {

    Slice<MinihomeEntity> findAllBy(Pageable pageable);
    Optional<MinihomeEntity> findByUserId(long userId);
}
