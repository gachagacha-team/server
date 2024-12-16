package gachagacha.gachagacha.minihome.repository;

import gachagacha.gachagacha.minihome.entity.Minihome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinihomeRepository extends JpaRepository<Minihome, Long> {
}
