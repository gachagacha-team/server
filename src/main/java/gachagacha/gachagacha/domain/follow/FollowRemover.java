package gachagacha.gachagacha.domain.follow;

import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.item.entity.FollowEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowRemover {

    private final FollowRepository followRepository;

    public void delete(User followee, User follower) {
        FollowEntity followEntity = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FOLLOW));
        followRepository.delete(followEntity);
    }

    public void deleteByFollowee(User user) {
        followRepository.deleteAllByFolloweeId(user.getId());
    }

    public void deleteByFollower(User user) {
        followRepository.deleteAllByFollowerId(user.getId());
    }
}
