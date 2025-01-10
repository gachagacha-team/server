package gachagacha.gachagacha.auth.oauth.client;

import gachagacha.gachagacha.auth.oauth.dto.UserInfo;
import gachagacha.gachagacha.auth.oauth.dto.github.GithubTokenRequest;
import gachagacha.gachagacha.auth.oauth.dto.github.GithubTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GithubOAuthClient implements OAuthClient {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String CLIENT_ID = "Iv23lica8LFNbzOrB595";
    private static final String CLIENT_SECRET = "4058722e6c8e35922b32ecb5bbcef6ff8ff5310c";
    private static final String REQUEST_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String REQUEST_USER_URL = "https://api.github.com/user";

    @Override
    public String fetchOAuthToken(String code) {
        GithubTokenResponse tokenResponse = restTemplate.postForObject(REQUEST_TOKEN_URL,
                new GithubTokenRequest(CLIENT_ID, CLIENT_SECRET, code),
                GithubTokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    @Override
    public UserInfo fetchOAuthUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(REQUEST_USER_URL,
                HttpMethod.GET,
                entity,
                Map.class
        );
        Map<String, Object> body = response.getBody();

        Long id = ((Number) body.get("id")).longValue();
        String login = (String) body.get("login");
        String avatarUrl = (String) body.get("avatar_url");

        return new UserInfo(id, login, avatarUrl);
    }
}
