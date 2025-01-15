package gachagacha.gachagacha.auth.oauth.client;

import gachagacha.gachagacha.auth.oauth.dto.kakao.KakaoTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KakaoOAuthClient implements OAuthClient {

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String REQUEST_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String REQUEST_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    @Value(value = "${oauth.kakao.client_id}")
    private String CLIENT_ID;

    @Value(value = "${oauth.kakao.redirect_url}")
    private String REDIRECT_URL;


    @Override
    public String fetchOAuthToken(String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> httpBodies = new LinkedMultiValueMap<>();
        httpBodies.add("grant_type", "authorization_code");
        httpBodies.add("client_id", CLIENT_ID);
        httpBodies.add("redirect_uri", REDIRECT_URL);
        httpBodies.add("code", code);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpBodies, httpHeaders);

        KakaoTokenResponse tokenResponse = restTemplate.postForObject(REQUEST_TOKEN_URL,
                httpEntity,
                KakaoTokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    @Override
    public Long fetchOAuthLoginId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(REQUEST_USER_URL,
                HttpMethod.GET,
                httpEntity,
                Map.class
        );
        Map<String, Object> body = response.getBody();
        return (Long) body.get("id");
    }
}
