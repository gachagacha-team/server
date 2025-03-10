package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.user.User;
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

    public static ExploreMinihomeResponse of(Minihome minihome, User user) {
        return new ExploreMinihomeResponse(
                user.getNickname(),
                minihome.getTotalVisitorCnt(),
               user.getProfile().getId()
        );
    }
}
