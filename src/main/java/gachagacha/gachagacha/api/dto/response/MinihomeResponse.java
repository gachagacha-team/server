package gachagacha.gachagacha.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MinihomeResponse {

    @JsonProperty(value = "isOwner")
    private boolean isOwner;

    @JsonProperty(value = "nickname")
    private String nickname;

    @JsonProperty(value = "score")
    private int score;

    @JsonProperty(value = "followersCnt")
    private int followersCnt;

    @JsonProperty(value = "followingCnt")
    private int followingCnt;

    @JsonProperty(value = "totalVisitorCnt")
    private int totalVisitorCnt;

    @JsonProperty(value = "profileImageStoreFileName")
    private String profileImageStoreFileName;

    @JsonProperty(value = "isFollowing")
    private boolean isFollowing;

    public static MinihomeResponse of(User currentUser, User minihomeUser, Minihome minihome, int followersCnt, int followingsCnt, boolean isFollowing, String profileImageApiEndpoint) {
        return new MinihomeResponse(
                minihomeUser.getNickname().equals(currentUser.getNickname()),
                minihomeUser.getNickname(),
                minihomeUser.getScore().getScore(),
                followersCnt,
                followingsCnt,
                minihome.getTotalVisitorCnt(),
                profileImageApiEndpoint + minihomeUser.getProfileImage().getStoreFileName(),
                isFollowing
        );
    }
}
