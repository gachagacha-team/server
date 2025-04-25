package gachagacha.domain.follow;

import gachagacha.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository {

    Long save(Follow follow);

    Slice<Follow> findByFolloweeId(long followeeId, Pageable pageable);

    List<Follow> findByFolloweeId(long followeeId);

    Slice<Follow> findByFollowerId(long followerId, Pageable pageable);

    List<Follow> findByFollowerId(long followerId);

    Optional<Follow> findByFolloweeIdAndFollowerId(long followeeId, long followerId);

    void deleteAllByFolloweeId(long userId);

    void deleteAllByFollowerId(long userId);

    boolean existsByFolloweeAndFollower(User followee, User follower);

    Slice<Follow> findByFollowee(User user, Pageable pageable);

    Slice<Follow> findByFollower(User user, Pageable pageable);

    void delete(Follow follow);
}
