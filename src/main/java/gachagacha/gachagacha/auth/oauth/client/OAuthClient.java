package gachagacha.gachagacha.auth.oauth.client;

import gachagacha.gachagacha.auth.oauth.dto.UserInfo;

public interface OAuthClient {
    String fetchOAuthToken(String code);
    UserInfo fetchOAuthUserInfo(String accessToken);
}
