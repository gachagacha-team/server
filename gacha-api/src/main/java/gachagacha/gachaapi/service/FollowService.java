package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.follow.Follow;
import gachagacha.domain.follow.FollowRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public long follow(User followee, User follower) {
        validateSelfFollow(followee, follower);
        validateDuplicatedFollow(followee, follower);
        return followRepository.save(Follow.of(followee.getId(), follower.getId()));
    }

    private void validateSelfFollow(User followee, User follower) {
        if (follower.getId() == followee.getId()) {
            throw new BusinessException(ErrorCode.CANNOT_SELF_FOLLOW);
        }
    }

    private void validateDuplicatedFollow(User followee, User follower) {
        if (followRepository.existsByFolloweeAndFollower(followee, follower)) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWING);
        }
    }

    public int readFollowersCnt(User user) {
        return followRepository.findByFolloweeId(user.getId()).size();
    }

    public int readFollowingsCnt(User user) {
        return followRepository.findByFollowerId(user.getId()).size();
    }

    public Slice<Follow> getFollowers(String nickname, Pageable pageable) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return followRepository.findByFollowee(user, pageable);
    }

    public Slice<Follow> getFollowings(String nickname, Pageable pageable) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return followRepository.findByFollower(user, pageable);
    }

    public boolean isFollowing(User follower, User followee) {
        return followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .isPresent();
    }

    public void removeFollow(User followee, User follower) {
        Follow follow = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FOLLOW));
        followRepository.delete(follow);
    }
}
