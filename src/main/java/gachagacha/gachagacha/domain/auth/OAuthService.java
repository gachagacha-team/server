package gachagacha.gachagacha.domain.auth;

import gachagacha.gachagacha.domain.user.SocialType;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserReader;
import gachagacha.gachagacha.jwt.JwtDto;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.domain.auth.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.domain.auth.oauth.client.KakaoOAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserReader userReader;
    private final JwtUtils jwtUtils;
    private final TokenProcessor tokenProcessor;

    public long authWithGithub(String code) {
        String accessToken = githubOAuthClient.fetchOAuthToken(code);
        long loginId = githubOAuthClient.fetchOAuthLoginId(accessToken);
        return loginId;
//        return generateRedirectUrl(loginId, SocialType.GITHUB);
    }

    public long authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        long loginId = kakaoOAuthClient.fetchOAuthLoginId(accessToken);
        return loginId;
//        return generateRedirectUrl(loginId, SocialType.KAKAO);
    }

//    private String generateRedirectUrl(long loginId, SocialType socialType) {
//        // 가입된 사용자 -> 로그인(토큰 발급), 새로운 사용자 -> 회원가입 폼으로
//        Optional<User> optionalUser = userReader.findBySocialTypeAndLoginId(socialType, loginId);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            JwtDto jwtDto = jwtUtils.generateJwt(user);
//            tokenProcessor.saveRefreshToken(jwtDto.getRefreshToken(), null);
////            return "https://gacha-ruddy.vercel.app/auth"
////                    + "?accessToken=" + jwtDto.getAccessToken()
////                    + "&refreshToken=" + jwtDto.getRefreshToken();
//            return "http://localhost:5173/auth"
//                    + "?accessToken=" + jwtDto.getAccessToken()
//                    + "&refreshToken=" + jwtDto.getRefreshToken();
//        } else {
////            return "https://gacha-ruddy.vercel.app/join"
////                    + "?socialType=" + socialType.getName()
////                    + "&loginId=" + loginId;
//            return "http://localhost:5173/join"
//                    + "?socialType=" + socialType.getName()
//                    + "&loginId=" + loginId;
//        }
//    }

    public void unlink(User user) {
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoOAuthClient.unlink(user.getLoginId());
        }
    }
}
