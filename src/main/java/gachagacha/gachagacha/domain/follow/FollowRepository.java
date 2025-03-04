package gachagacha.gachagacha.domain.follow;

import gachagacha.gachagacha.domain.item.entity.FollowEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Slice<FollowEntity> findByFolloweeId(long followeeId, Pageable pageable);

    List<FollowEntity> findByFolloweeId(long followeeId);

    Slice<FollowEntity> findByFollowerId(long followerId, Pageable pageable);

    List<FollowEntity> findByFollowerId(long followerId);

    Optional<FollowEntity> findByFolloweeIdAndFollowerId(long followeeId, long followerId);

    void deleteAllByFolloweeId(long userId);

    void deleteAllByFollowerId(long userId);
}
