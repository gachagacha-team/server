package gachagacha.domain.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private Long id;
    private Long followeeId;
    private Long followerId;

    public static Follow of(long followeeId, long followerId) {
        Follow follow = new Follow();
        follow.followeeId = followeeId;
        follow.followerId = followerId;
        return follow;
    }
}
