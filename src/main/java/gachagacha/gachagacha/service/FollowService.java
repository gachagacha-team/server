package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.Follow;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.implementation.follow.FollowReader;
import gachagacha.gachagacha.implementation.follow.FollowRemover;
import gachagacha.gachagacha.implementation.follow.FollowAppender;
import gachagacha.gachagacha.implementation.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserReader userReader;
    private final FollowReader followReader;
    private final FollowAppender followAppender;
    private final FollowRemover followRemover;

    public long follow(User followee, User follower) {
        validateSelfFollow(followee, follower);
        validateDuplicatedFollow(followee, follower);
        return followAppender.save(Follow.of(followee.getId(), follower.getId()));
    }

    private void validateSelfFollow(User followee, User follower) {
        if (follower.getId() == followee.getId()) {
            throw new BusinessException(ErrorCode.CANNOT_SELF_FOLLOW);
        }
    }

    private void validateDuplicatedFollow(User followee, User follower) {
        if (followReader.existsByFolloweeAndFollower(followee, follower)) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWING);
        }
    }

    public void unfollow(User followee, User follower) {
        followRemover.delete(followee, follower);
    }

    public int readFollowersCnt(User user) {
        return followReader.findByFollowee(user).size();
    }

    public int readFollowingsCnt(User user) {
        return followReader.findByFollower(user).size();
    }

    public Slice<Follow> getFollowers(String nickname, Pageable pageable) {
        User user = userReader.findByNickname(nickname);
        return followReader.findByFollowee(user, pageable);
    }

    public Slice<Follow> getFollowings(String nickname, Pageable pageable) {
        User user = userReader.findByNickname(nickname);
        return followReader.findByFollower(user, pageable);
    }

    public boolean isFollowing(User follower, User followee) {
        return followReader.existsByFolloweeAndFollower(followee, follower);
    }
}
