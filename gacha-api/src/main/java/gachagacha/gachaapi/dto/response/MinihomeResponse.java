package gachagacha.gachaapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.user.User;
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

    @JsonProperty(value = "profileId")
    private long profileId;

    @JsonProperty(value = "isFollowing")
    private boolean isFollowing;

    public static MinihomeResponse of(User currentUser, User minihomeUser, Minihome minihome, int followersCnt, int followingsCnt, boolean isFollowing) {
        return new MinihomeResponse(
                minihomeUser.getNickname().equals(currentUser.getNickname()),
                minihomeUser.getNickname(),
                minihomeUser.getScore().getScore(),
                followersCnt,
                followingsCnt,
                minihome.getTotalVisitorCnt(),
                minihomeUser.getProfile().getId(),
                isFollowing
        );
    }
}
