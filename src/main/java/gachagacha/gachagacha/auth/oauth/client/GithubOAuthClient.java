package gachagacha.gachagacha.auth.oauth.client;

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
    private static final String REQUEST_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String REQUEST_USER_URL = "https://api.github.com/user";


    @Value(value = "${oauth.github.client_id}")
    private String CLIENT_ID;

    @Value(value = "${oauth.github.client_secret}")
    private String CLIENT_SECRET;

    @Override
    public String fetchOAuthToken(String code) {
        GithubTokenResponse tokenResponse = restTemplate.postForObject(REQUEST_TOKEN_URL,
                new GithubTokenRequest(CLIENT_ID, CLIENT_SECRET, code),
                GithubTokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    @Override
    public Long fetchOAuthLoginId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(REQUEST_USER_URL,
                HttpMethod.GET,
                entity,
                Map.class
        );
        Map<String, Object> body = response.getBody();

        return ((Number) body.get("id")).longValue();
    }
}
