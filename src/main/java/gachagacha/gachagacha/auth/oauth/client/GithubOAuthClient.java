package gachagacha.gachagacha.auth.oauth.client;

import gachagacha.gachagacha.auth.oauth.dto.github.GithubTokenRequest;
import gachagacha.gachagacha.auth.oauth.dto.github.GithubTokenResponse;
import gachagacha.gachagacha.auth.oauth.dto.github.GithubUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
    public Long fetchOAuthUserId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubUserResponse> exchange = restTemplate.exchange(requestUserUrl,
                HttpMethod.GET,
                entity,
                GithubUserResponse.class
        );
        return exchange.getBody().getId();
    }
}
