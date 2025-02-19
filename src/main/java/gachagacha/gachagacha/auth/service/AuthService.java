package gachagacha.gachagacha.auth.service;

import gachagacha.gachagacha.auth.RefreshTokenRepository;
import gachagacha.gachagacha.auth.entity.RefreshToken;
import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.minihome.repository.GuestbookRepository;
import gachagacha.gachagacha.trade.repository.TradeRepository;
import gachagacha.gachagacha.user.dto.JoinRequest;
import gachagacha.gachagacha.user.entity.ProfileImage;
import gachagacha.gachagacha.user.entity.SocialType;
import gachagacha.gachagacha.auth.oauth.client.GithubOAuthClient;
import gachagacha.gachagacha.auth.oauth.client.KakaoOAuthClient;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.AttendanceRepository;
import gachagacha.gachagacha.user.repository.FollowRepository;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${file.profile}")
    private String fileDir;

    @Value("${image.api.endpoints.profile}")
    private String profileImageApiEndpoint;

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final GithubOAuthClient githubOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FollowRepository followRepository;
    private final AttendanceRepository attendanceRepository;
    private final TradeRepository tradeRepository;
    private final GuestbookRepository guestbookRepository;

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
            JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname(), profileImageApiEndpoint + user.getProfileImage().getStoreFileName());
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

    public String join(JoinRequest joinRequest, MultipartFile file) throws IOException {
        SocialType socialType = SocialType.find(joinRequest.getSocialType());

        validateDuplicatedUser(socialType, joinRequest.getLoginId());
        validateDuplicatedNickname(joinRequest.getNickname());

        ProfileImage profileImage;
        if (file == null) {
            profileImage = ProfileImage.createDefault();
        } else {
            profileImage = ProfileImage.create(file.getOriginalFilename());
            storeProfileImage(file, profileImage.getStoreFileName());
        }

        User user = User.create(socialType, joinRequest.getLoginId(), joinRequest.getNickname(), profileImage);
        userRepository.save(user);

        JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname(), user.getProfileImage().getStoreFileName());
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

    private void storeProfileImage(MultipartFile file, String storeFileName) throws IOException {
        String directoryPath = System.getProperty("user.dir") + fileDir;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        file.transferTo(new File(directoryPath + storeFileName));
    }

    public JwtDto renewTokens(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));

        refreshTokenRepository.delete(refreshToken);
        User user = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        JwtDto jwtDto = jwtUtils.generateJwt(user.getNickname(), profileImageApiEndpoint + user.getProfileImage().getStoreFileName());
        refreshTokenRepository.save(new RefreshToken(jwtDto.getRefreshToken()));
        return jwtDto;
    }

    public void logout(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public void withdraw(HttpServletRequest request) {
        User user = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        // follow 엔티티 삭제
        followRepository.findByFollowee(user).stream()
                .forEach(follow -> followRepository.delete(follow));
        followRepository.findByFollower(user).stream()
                .forEach(follow -> followRepository.delete(follow));

        // attendance 엔티티 삭제
        attendanceRepository.findByUser(user).stream()
                .forEach(attendance -> attendanceRepository.delete(attendance));

        // trade 엔티티 soft delete
        tradeRepository.findBySeller(user).stream()
                .forEach(trade -> trade.softDeleteBySeller());
        tradeRepository.findByBuyer(user).stream()
                .forEach(trade -> trade.softDeleteByBuyer());

        // guestbook 엔티티 soft delete
        guestbookRepository.findByMinihome(user.getMinihome()).stream()
                .forEach(guestbook -> guestbook.softDeleteByMinihomeUser());
        guestbookRepository.findByUser(user).stream()
                .forEach(guestbook -> guestbook.softDeleteByUser());

        // user, userItem, minihome 엔티티 삭제
        userRepository.delete(user);

        // RT 삭제
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));
        refreshTokenRepository.delete(refreshToken);

        // 소셜 로그인 해제 (TODO: 깃허브 추가하기)
        if (user.getSocialType() == SocialType.KAKAO) {
            kakaoOAuthClient.unlink(user.getLoginId());
        }
    }
}
