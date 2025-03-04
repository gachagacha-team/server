package gachagacha.gachagacha.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {

    private long userId;
    private String nickname;
    private String profileImageStoreFileName;

    @JsonProperty("isFollowing")
    private boolean isFollowing; // 조회하는 사용자가 이 사용자를 팔로우하는지 여부

    @JsonProperty("isRemovable")
    private boolean isRemovable; // 자신의 팔로워 목록을 조회하는 경우 true

    @JsonProperty("isCurrentUser")
    private boolean isCurrentUser; // 조회된 사용자가 현재 조회 중인 사용자 본인인지 여부

    public static FollowerResponse of(User follower, User followee, User currentUser, boolean isFollowing, String profileImageApiEndpoint) {
        return new FollowerResponse(
                follower.getId(),
                follower.getNickname(),
                profileImageApiEndpoint + follower.getProfileImage().getStoreFileName(),
                isFollowing,
                currentUser.getId() == followee.getId(),
                currentUser.getId() == follower.getId());
    }

    public long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageStoreFileName() {
        return profileImageStoreFileName;
    }
}
