package gachagacha.gachagacha.auth.oauth.client;

import gachagacha.gachagacha.auth.oauth.dto.UserInfo;
import gachagacha.gachagacha.auth.oauth.dto.github.GithubTokenRequest;
import gachagacha.gachagacha.auth.oauth.dto.github.GithubTokenResponse;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${oauth2.client.registration.github.client_id}")
    private String clientId;

    @Value("${oauth2.client.registration.github.client_secret}")
    private String clientSecret;

    @Value("${oauth2.client.registration.github.request_token_url}")
    private String requestTokenUrl;

    @Value("${oauth2.client.registration.github.request_user_url}")
    private String requestUserUrl;

    @Override
    public String fetchOAuthToken(String code) {
        GithubTokenResponse tokenResponse = restTemplate.postForObject(requestTokenUrl,
                new GithubTokenRequest(clientId, clientSecret, code),
                GithubTokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    @Override
    public UserInfo fetchOAuthUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(requestUserUrl,
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
