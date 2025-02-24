package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Minihome;
import gachagacha.gachagacha.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
