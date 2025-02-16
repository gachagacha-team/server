package gachagacha.gachagacha.minihome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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

    @JsonProperty(value = "layout")
    private String layout;
}
