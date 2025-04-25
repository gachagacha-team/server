package gachagacha.db.follow;

import gachagacha.domain.follow.Follow;
import gachagacha.domain.follow.FollowRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FollowEntityRepository implements FollowRepository {

    private final FollowJpaRepository followJpaRepository;

    @Override
    public Long save(Follow follow) {
        FollowEntity followEntity = followJpaRepository.save(FollowEntity.fromFollow(follow));
        return followEntity.getId();
    }

    @Override
    public Slice<Follow> findByFolloweeId(long followeeId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Follow> findByFolloweeId(long followeeId) {
        return followJpaRepository.findByFolloweeId(followeeId).stream()
                .map(followEntity -> followEntity.toFollow())
                .toList();
    }

    @Override
    public Slice<Follow> findByFollowerId(long followerId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Follow> findByFollowerId(long followerId) {
        return followJpaRepository.findByFollowerId(followerId).stream()
                .map(followEntity -> followEntity.toFollow())
                .toList();
    }

    @Override
    public Optional<Follow> findByFolloweeIdAndFollowerId(long followeeId, long followerId) {
        return followJpaRepository.findByFolloweeIdAndFollowerId(followeeId, followerId)
                .map(followEntity -> followEntity.toFollow());
    }

    @Override
    public void deleteAllByFolloweeId(long userId) {
        followJpaRepository.deleteAllByFolloweeId(userId);
    }

    @Override
    public void deleteAllByFollowerId(long userId) {
        followJpaRepository.deleteAllByFollowerId(userId);
    }

    @Override
    public boolean existsByFolloweeAndFollower(User followee, User follower) {
        return followJpaRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .isPresent();
    }

    @Override
    public Slice<Follow> findByFollowee(User user, Pageable pageable) {
        return followJpaRepository.findByFolloweeId(user.getId(), pageable)
                .map(followEntity -> followEntity.toFollow());
    }

    @Override
    public Slice<Follow> findByFollower(User user, Pageable pageable) {
        return followJpaRepository.findByFollowerId(user.getId(), pageable)
                .map(followEntity -> followEntity.toFollow());
    }

    @Override
    public void delete(Follow follow) {
        followJpaRepository.delete(FollowEntity.fromFollow(follow));
    }
}
