package gachagacha.gachagacha.implementation.follow;

import gachagacha.gachagacha.repository.FollowRepository;
import gachagacha.gachagacha.domain.Follow;
import gachagacha.gachagacha.entity.FollowEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowAppender {

    private final FollowRepository followRepository;

    public long save(Follow follow) {
        FollowEntity savedFollowEntity = followRepository.save(follow.toFollowEntity());
        return savedFollowEntity.getId();
    }
}
