package gachagacha.gachagacha.auth.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {

    private long id;
    private String nickname;
    private String profileImageUrl;
}
