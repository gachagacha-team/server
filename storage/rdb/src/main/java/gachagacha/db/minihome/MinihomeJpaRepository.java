package gachagacha.db.minihome;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinihomeJpaRepository extends JpaRepository<MinihomeEntity, Long> {

    Slice<MinihomeEntity> findAllBy(Pageable pageable);

    Optional<MinihomeEntity> findByUserId(long userId);

    @Modifying
    @Query("update MinihomeEntity m " +
            "set m.totalVisitorCnt = m.totalVisitorCnt + 1 " +
            "where m.id = :id")
    void increaseVisitorCount(@Param("id") Long minihomeId);
}
