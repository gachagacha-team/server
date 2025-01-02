package gachagacha.gachagacha.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.gachagacha.auth.jwt.JwtDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @JsonProperty(value = "isNewUser")
    private boolean isNewUser;

    @JsonProperty(value = "jwtDto")
    private JwtDto jwtDto;

    @JsonProperty(value = "loginType")
    private String loginType;

    @JsonProperty(value = "loginId")
    private Long loginId;

    @JsonProperty(value = "nickname")
    private String nickname;

    @JsonProperty(value = "profileUrl")
    private String profileUrl;
}
