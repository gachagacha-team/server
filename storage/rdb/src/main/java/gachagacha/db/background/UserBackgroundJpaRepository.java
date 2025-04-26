package gachagacha.db.background;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBackgroundJpaRepository extends JpaRepository<UserBackgroundEntity, Long> {

    List<UserBackgroundEntity> findByUserId(long userId);
}
