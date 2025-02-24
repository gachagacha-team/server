package gachagacha.gachagacha.service;

import gachagacha.gachagacha.implementation.minihome.MinihomeReader;
import gachagacha.gachagacha.implementation.minihome.MinihomeRemover;
import gachagacha.gachagacha.implementation.user.UserRemover;
import gachagacha.gachagacha.implementation.userItem.UserItemRemover;
import gachagacha.gachagacha.repository.RefreshTokenRepository;
import gachagacha.gachagacha.domain.Minihome;
import gachagacha.gachagacha.domain.ProfileImage;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.RefreshTokenEntity;
import gachagacha.gachagacha.implementation.attendance.AttendanceRemover;
import gachagacha.gachagacha.implementation.follow.FollowRemover;
import gachagacha.gachagacha.implementation.guestbook.GuestbookRemover;
import gachagacha.gachagacha.implementation.minihome.MinihomeAppender;
import gachagacha.gachagacha.implementation.trade.TradeRemover;
import gachagacha.gachagacha.implementation.user.UserReader;
import gachagacha.gachagacha.implementation.user.UserAppender;
import gachagacha.gachagacha.jwt.JwtDto;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.domain.SocialType;
import gachagacha.gachagacha.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.oauth.client.KakaoOAuthClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileImageService profileImageService;
    private final UserAppender userAppender;
    private final MinihomeAppender minihomeAppender;
    private final UserReader userReader;
    private final FollowRemover followRemover;
    private final AttendanceRemover attendanceRemover;
    private final TradeRemover tradeRemover;
    private final GuestbookRemover guestbookRemover;
    private final JwtUtils jwtUtils;
    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MinihomeRemover minihomeRemover;
    private final MinihomeReader minihomeReader;
    private final UserRemover userRemover;
    private final UserItemRemover userItemRemover;

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
        Optional<User> optionalUser = userReader.findBySocialTypeAndLoginId(socialType, loginId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = jwtUtils.generateJwt(user);
            refreshTokenRepository.save(new RefreshTokenEntity(jwtDto.getRefreshToken()));
            return "http://localhost:5173/auth"
                    + "?accessToken=" + jwtDto.getAccessToken()
                    + "&refreshToken=" + jwtDto.getRefreshToken();
        } else {
            return "http://localhost:5173/join"
                    + "?socialType=" + socialType.getName()
                    + "&loginId=" + loginId;
        }
    }

    public User join(String nickname, SocialType socialType, Long loginId, MultipartFile file) throws IOException {
        validateDuplicatedUser(socialType, loginId);
        validateDuplicatedNickname(nickname);

        ProfileImage profileImage = ProfileImage.of(file);
        if (file != null) {
            profileImageService.storeProfileImage(file, profileImage.getStoreFileName());
        }

        User user = User.of(nickname, socialType, loginId, profileImage);
        long userId = userAppender.save(user);

        Minihome minihome = Minihome.of(userId);
        minihomeAppender.save(minihome);

        return user;
    }

    private void validateDuplicatedUser(SocialType socialType, long loginId) {
        if (userReader.existsBySocialTypeAndLoginId(socialType, loginId)) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_REGISTRATION);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (userReader.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    public JwtDto renewTokens(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));

        refreshTokenRepository.delete(refreshTokenEntity);
        User user = userReader.findByNickname(jwtUtils.getUserNicknameFromHeader(request));
        JwtDto jwtDto = jwtUtils.generateJwt(user);
        refreshTokenRepository.save(new RefreshTokenEntity(jwtDto.getRefreshToken()));
        return jwtDto;
    }

    public void logout(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));
        refreshTokenRepository.delete(refreshTokenEntity);
    }

    @Transactional
    public void withdraw(String nickname, String refreshToken) {
        User user = userReader.findByNickname(nickname);
        Minihome minihome = minihomeReader.findByUser(user);

        // trade 엔티티 soft delete
        tradeRemover.softDeleteBySeller(user);
        tradeRemover.softDeleteByBuyer(user);

        // guestbook 엔티티 soft delete
        guestbookRemover.softDeleteByMinihome(minihome);
        guestbookRemover.softDeleteByAuthor(user);

        // follow 엔티티 삭제
        followRemover.deleteByFollowee(user);
        followRemover.deleteByFollower(user);

        // RT 삭제
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));
        refreshTokenRepository.delete(refreshTokenEntity);

        // userItem, minihome, attendance, user 엔티티 삭제
        userItemRemover.deleteByUser(user);
        minihomeRemover.delete(minihome);
        attendanceRemover.deleteByUser(user);
        userRemover.delete(user);

        // 소셜 로그인 해제 (TODO: 깃허브 추가하기)
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoOAuthClient.unlink(user.getLoginId());
        }
    }
}
