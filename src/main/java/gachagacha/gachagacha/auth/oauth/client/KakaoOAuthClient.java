package gachagacha.gachagacha.auth.oauth.client;

import gachagacha.gachagacha.auth.oauth.dto.UserInfo;
import gachagacha.gachagacha.auth.oauth.dto.kakao.KakaoTokenResponse;
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
    private static final String CLIENT_ID = "9c35100cd45c705417bf81e23e8c7734";
    private static final String REQUEST_USER_URL = "https://kapi.kakao.com/v2/user/me";
//    private static final String REDIRECT_URL = "http://localhost:8085/login/oauth2/code/kakao";
    private static final String REDIRECT_URL = "http://61.79.183.245:80/login/oauth2/code/kakao";
    private static final String REQUEST_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

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
    public UserInfo fetchOAuthUserInfo(String accessToken) {
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
        Long id = (Long) body.get("id");
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        return new UserInfo(id, nickname, profileImage);
    }
}
