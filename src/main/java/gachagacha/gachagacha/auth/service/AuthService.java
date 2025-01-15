package gachagacha.gachagacha.auth.service;

import gachagacha.gachagacha.auth.RefreshTokenRepository;
import gachagacha.gachagacha.auth.entity.RefreshToken;
import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.user.dto.JoinRequest;
import gachagacha.gachagacha.user.entity.SocialType;
import gachagacha.gachagacha.auth.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.auth.oauth.client.KakaoOAuthClient;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final RefreshTokenRepository refreshTokenRepository;

    public String authWithGithub(String code) {
        String accessToken = githubOAuthClient.fetchOAuthToken(code);
        long loginId = githubOAuthClient.fetchOAuthLoginId(accessToken);
        return generateRedirectUrl(loginId, SocialType.GITHUB);
    }

    public String authWithKakao(String code) {
        String accessToken = kakaoOAuthClient.fetchOAuthToken(code);
        long loginId = kakaoOAuthClient.fetchOAuthLoginId(accessToken);
        return generateRedirectUrl(loginId, SocialType.KAKAO);
    }

    private String generateRedirectUrl(long loginId, SocialType socialType) {
        Optional<User> optionalUser = userRepository.findBySocialTypeAndLoginId(socialType, loginId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname(), user.getProfileImageUrl());
            refreshTokenRepository.save(new RefreshToken(jwtDto.getRefreshToken()));
            return "http://localhost:5173/auth"
                    + "?accessToken=" + jwtDto.getAccessToken()
                    + "&refreshToken=" + jwtDto.getRefreshToken();
        } else {
            return "http://localhost:5173/join"
                    + "?socialType=" + socialType.getName()
                    + "&loginId=" + loginId;
        }
    }

    public String join(JoinRequest joinRequest) {
        SocialType socialType = SocialType.find(joinRequest.getSocialType());

        validateDuplicatedUser(socialType, joinRequest.getLoginId());
        validateDuplicatedNickname(joinRequest.getNickname());

        User user = User.create(socialType, joinRequest.getLoginId(), joinRequest.getNickname(), joinRequest.getProfileUrl());
        userRepository.save(user);
        JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname(), user.getProfileImageUrl());
        refreshTokenRepository.save(new RefreshToken(jwtDto.getRefreshToken()));
        return "http://localhost:5173/auth"
                + "?accessToken=" + jwtDto.getAccessToken()
                + "&refreshToken=" + jwtDto.getRefreshToken();
    }

    private void validateDuplicatedUser(SocialType socialType, long loginId) {
        if (userRepository.findBySocialTypeAndLoginId(socialType, loginId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_REGISTRATION);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    public JwtDto renewTokens(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));

        refreshTokenRepository.delete(refreshToken);
        User user = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname(), user.getProfileImageUrl());
        refreshTokenRepository.save(new RefreshToken(jwtDto.getRefreshToken()));
        return jwtDto;
    }

    public void logout(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));

        refreshTokenRepository.delete(refreshToken);
    }
}
