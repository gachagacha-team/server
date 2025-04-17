package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private String nickname;
    private long profileId;

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(user.getNickname(), user.getProfile().getId());
    }
}
