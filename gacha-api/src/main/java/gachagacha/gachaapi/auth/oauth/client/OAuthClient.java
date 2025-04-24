package gachagacha.gachaapi.auth.oauth.client;

public interface OAuthClient {
    String fetchOAuthToken(String code);
    Long fetchOAuthLoginId(String accessToken);
}
