package gachagacha.gachagacha.domain.auth.oauth.client;

public interface OAuthClient {
    String fetchOAuthToken(String code);
    Long fetchOAuthLoginId(String accessToken);
}
