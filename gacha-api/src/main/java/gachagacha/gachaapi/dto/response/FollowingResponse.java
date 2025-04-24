package gachagacha.gachaapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FollowingResponse {
    private Long userId;
    private String nickname;
    private long profileId;

    @JsonProperty("isFollowing")
    private boolean isFollowing;

    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser; // 조회된 사용자가 현재 조회 중인 사용자 본인인지 여부

    public static FollowingResponse of(User followee, User currentUser, boolean isFollowing) {
        return new FollowingResponse(
                followee.getId(),
                followee.getNickname(),
                followee.getProfile().getId(),
                isFollowing,
                followee.getId() == currentUser.getId());
    }
    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }
}
