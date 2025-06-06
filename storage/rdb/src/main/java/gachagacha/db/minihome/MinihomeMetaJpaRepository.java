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
public interface MinihomeMetaJpaRepository extends JpaRepository<MinihomeMetaEntity, Long> {

    @Modifying
    @Query("update MinihomeMetaEntity m " +
            "set m.likeCount = m.likeCount + 1 " +
            "where m.id = :id")
    void increaseLikeCount(@Param("id") Long minihomeId);

    @Modifying
    @Query("update MinihomeMetaEntity m " +
            "set m.likeCount = m.likeCount - 1 " +
            "where m.id = :id")
    void decreaseLikeCount(@Param("id") Long minihomeId);

    Optional<MinihomeMetaEntity> findByMinihomeId(Long minihomeId);

    @Query("select mm.minihomeId " +
            "from MinihomeMetaEntity mm " +
            "where mm.likeCount < :likeCount or (mm.likeCount = :likeCount and mm.minihomeId < :minihomeId) " +
            "order by mm.likeCount desc, mm.minihomeId desc")
    Slice<Long> findMinihomeIdsOrderByLikeCountDescAndMinihomeIdDesc(Pageable pageable, long likeCount, long minihomeId);
}
