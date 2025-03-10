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
    private String profileImageStoreFileName;

    public static ExploreMinihomeResponse of(Minihome minihome, User user, String profileImageApiEndpoint) {
        return new ExploreMinihomeResponse(
                user.getNickname(),
                minihome.getTotalVisitorCnt(),
                profileImageApiEndpoint + user.getProfileImage().getStoreFileName()
        );
    }
}
