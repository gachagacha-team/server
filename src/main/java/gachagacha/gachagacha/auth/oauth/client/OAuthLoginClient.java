package gachagacha.gachagacha.auth.oauth.client;

public interface OAuthLoginClient {
    String fetchOAuthToken(String code);
    Long fetchOAuthUserId(String accessToken);
}
