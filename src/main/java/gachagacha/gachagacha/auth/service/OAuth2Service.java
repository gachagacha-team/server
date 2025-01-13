package gachagacha.gachagacha.auth.service;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.auth.oauth.dto.UserInfo;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.auth.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.auth.oauth.client.KakaoOAuthClient;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private static final String REDIRECT_BASIC_URL = "http://localhost:5173/auth";
//    private static final String DEFAULT_PROFILE_IMAGE_URL = "http://localhost:8085/image/profile";
    private static final String DEFAULT_PROFILE_IMAGE_URL = "http://61.79.183.245:80/image/profile";

    public String authWithGithub(String code) {
        String accessToken = githubOAuthClient.fetchOAuthToken(code);
        UserInfo userInfo = githubOAuthClient.fetchOAuthUserInfo(accessToken);
        return generateRedirectUrl(userInfo, LoginType.GITHUB);
    }

    public String authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        UserInfo userInfo = kakaoOAuthClient.fetchOAuthUserInfo(accessToken);
        return generateRedirectUrl(userInfo, LoginType.KAKAO);
    }

    private String generateRedirectUrl(UserInfo userInfo, LoginType loginType) {
        Optional<User> optionalUser = userRepository.findByLoginTypeAndLoginId(loginType, userInfo.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user.getId());
            String encodedNickname = URLEncoder.encode(user.getNickname(), StandardCharsets.UTF_8);

            return REDIRECT_BASIC_URL
                    + "?isNewUser=" + false
                    + "&nickname=" + encodedNickname
                    + "&profileUrl=" + user.getProfileImageUrl()
                    + "&loginType="
                    + "&loginId="
                    + "&accessToken=" + jwtDto.getAccessToken()
                    + "&refreshToken=" + jwtDto.getRefreshToken();
        } else {
            String encodedNickname = URLEncoder.encode(userInfo.getNickname(), StandardCharsets.UTF_8);
            return REDIRECT_BASIC_URL
                    + "?isNewUser=" + true
                    + "&nickname=" + encodedNickname
                    + "&profileUrl=" + getProfileImageUrl(userInfo, loginType)
                    + "&loginType=" + loginType.getName()
                    + "&loginId=" + userInfo.getId()
                    + "&accessToken="
                    + "&refreshToken=";
        }
    }

    private String getProfileImageUrl(UserInfo userInfo, LoginType loginType) {
        if (isDefaultProfileImage(userInfo, loginType)) {
            return DEFAULT_PROFILE_IMAGE_URL;
        }
        return userInfo.getProfileImageUrl();
    }

    private boolean isDefaultProfileImage(UserInfo userInfo, LoginType loginType) {
        if (loginType == LoginType.GITHUB && userInfo.getProfileImageUrl().startsWith("https://avatars.githubusercontent.com")) {
            return true;
        }
        if (loginType == LoginType.KAKAO && userInfo.getProfileImageUrl().equals("http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg")) {
            return true;
        }
        return false;
    }
}
