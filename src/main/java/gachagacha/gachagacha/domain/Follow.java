package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.FollowEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private long id;
    private long followeeId;
    private long followerId;

    public static Follow of(long followeeId, long followerId) {
        Follow follow = new Follow();
        follow.followeeId = followeeId;
        follow.followerId = followerId;
        return follow;
    }

    public FollowEntity toFollowEntity() {
        return new FollowEntity(
                0l,
                followeeId,
                followerId
        );
    }
}
