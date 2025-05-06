package gachagacha.gachaapi.dto.response;

import gachagacha.domain.meta.MinihomeMeta;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExploreMinihomeResponse {

    private String nickname;
    private int totalVisitorCnt;
    private long profileId;
    private long likeCount;

    public static ExploreMinihomeResponse of(Minihome minihome, User user, MinihomeMeta minihomeMeta) {
        return new ExploreMinihomeResponse(
                user.getNickname(),
                minihome.getTotalVisitorCnt(),
                user.getProfile().getId(),
                minihomeMeta.getLikeCount()
        );
    }
}
