package gachagacha.gachagacha.auth.oauth.dto;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private boolean isNewUser;
    private JwtDto jwtDto;
    private String loginType;
    private Long loginId;
}