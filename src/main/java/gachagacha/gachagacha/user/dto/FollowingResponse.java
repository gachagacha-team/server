package gachagacha.gachagacha.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class FollowingResponse {
    private Long userId;
    private String nickname;
    private String profileImageStoreFileName;

    @JsonProperty("isFollowing")
    private boolean isFollowing;

    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser; // 조회된 사용자가 현재 조회 중인 사용자 본인인지 여부

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageStoreFileName() {
        return profileImageStoreFileName;
    }
}
