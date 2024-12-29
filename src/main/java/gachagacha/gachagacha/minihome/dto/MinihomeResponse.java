package gachagacha.gachagacha.minihome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MinihomeResponse {

    private boolean isOwner;
    private long minihomeUserId;
    private String nickname;
    private int ranking;
    private int followersCnt;
    private int followingCnt;
    private int totalVisitorCnt;
    private String profileImageUrl;
    private String layout;
}
