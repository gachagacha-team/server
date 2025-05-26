package gachagacha.db.query;

import gachagacha.db.minihome.MinihomeMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExploreQueryRepository extends JpaRepository<MinihomeMetaEntity, Long> {

    @Query("select new gachagacha.db.query.ExploreQueryDto(m.id, u.id, u.nickname, u.profile, m.totalVisitorCnt, mm.likeCount, u.score, m.createdAt) " +
            "from MinihomeMetaEntity mm " +
            "join MinihomeEntity m on mm.minihomeId = m.id " +
            "join UserEntity u on m.userId = u.id " +
            "where m.id in :minihomeIds")
    List<ExploreQueryDto> findExploreDtosByMinihomeIds(@Param("minihomeIds") List<Long> minihomeIds);

    @Query("select new gachagacha.db.query.ExploreQueryDto(m.id, u.id, u.nickname, u.profile, m.totalVisitorCnt, mm.likeCount, u.score, m.createdAt) " +
            "from UserEntity u " +
            "join MinihomeEntity m on m.userId = u.id " +
            "join MinihomeMetaEntity mm on mm.minihomeId = m.id " +
            "where u.id in :userIds")
    List<ExploreQueryDto> findExploreDtosByUserIds(@Param("userIds")List<Long> userIds);
}
