package gachagacha.gachagacha.domain.follow;

import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowReader {

    private final FollowRepository followRepository;

    public boolean existsByFolloweeAndFollower(User followee, User follower) {
        return followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .isPresent();
    }

    public Slice<Follow> findByFollowee(User followee, Pageable pageable) {
        return followRepository.findByFolloweeId(followee.getId(), pageable)
                .map(followEntity -> followEntity.toFollow());
    }

    public List<Follow> findByFollowee(User followee) {
        return followRepository.findByFolloweeId(followee.getId())
                .stream()
                .map(followEntity -> followEntity.toFollow())
                .toList();
    }

    public List<Follow> findByFollower(User follower) {
        return followRepository.findByFollowerId(follower.getId())
                .stream()
                .map(followEntity -> followEntity.toFollow())
                .toList();
    }

    public Slice<Follow> findByFollower(User follower, Pageable pageable) {
        return followRepository.findByFollowerId(follower.getId(), pageable)
                .map(followEntity -> followEntity.toFollow());
    }
}
