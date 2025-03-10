package gachagacha.gachagacha.domain.auth;

import gachagacha.gachagacha.dd.DecorationProcessor;
import gachagacha.gachagacha.dd.dto.Decoration;
import gachagacha.gachagacha.domain.minihome.MinihomeReader;
import gachagacha.gachagacha.domain.minihome.MinihomeRemover;
import gachagacha.gachagacha.domain.item.UserItemRemover;
import gachagacha.gachagacha.domain.user.*;
import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.guestbook.ProfileImage;
import gachagacha.gachagacha.domain.attendance.AttendanceRemover;
import gachagacha.gachagacha.domain.follow.FollowRemover;
import gachagacha.gachagacha.domain.guestbook.GuestbookRemover;
import gachagacha.gachagacha.domain.minihome.MinihomeAppender;
import gachagacha.gachagacha.domain.trade.TradeRemover;
import gachagacha.gachagacha.jwt.JwtDto;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.domain.user.SocialType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${client.address}")
    private String clientAddress;

    private final ProfileImageService profileImageService;
    private final UserAppender userAppender;
    private final MinihomeAppender minihomeAppender;
    private final UserReader userReader;
    private final FollowRemover followRemover;
    private final AttendanceRemover attendanceRemover;
    private final TradeRemover tradeRemover;
    private final GuestbookRemover guestbookRemover;
    private final JwtUtils jwtUtils;
    private final TokenProcessor tokenProcessor;
    private final MinihomeRemover minihomeRemover;
    private final MinihomeReader minihomeReader;
    private final UserRemover userRemover;
    private final UserItemRemover userItemRemover;
    private final DecorationProcessor decorationProcessor;

    @Transactional
    public String join(String nickname, SocialType socialType, Long loginId, MultipartFile file) throws IOException {
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

        Decoration decoration = new Decoration(1l, null);
        decorationProcessor.save(decoration, userId);

        JwtDto jwtDto = jwtUtils.generateJwt(user);
        tokenProcessor.save(jwtDto.getRefreshToken());
        return clientAddress + "/auth"
                + "?accessToken=" + jwtDto.getAccessToken()
                + "&refreshToken=" + jwtDto.getRefreshToken();
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
        tokenProcessor.delete(jwtUtils.getRefreshTokenFromHeader(request));

        User user = userReader.findByNickname(jwtUtils.getUserNicknameFromHeader(request));
        JwtDto jwtDto = jwtUtils.generateJwt(user);
        tokenProcessor.save(jwtDto.getRefreshToken());
        return jwtDto;
    }

    public void logout(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);
        String refreshToken = jwtUtils.getRefreshTokenFromHeader(request);
        tokenProcessor.delete(refreshToken);
    }

    @Transactional
    public void withdraw(User user, String refreshToken) {
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
        tokenProcessor.delete(refreshToken);

        // userItem, minihome, attendance, user 엔티티 삭제
        userItemRemover.deleteByUser(user);
        minihomeRemover.delete(minihome);
        attendanceRemover.deleteByUser(user);
        userRemover.delete(user);
    }

    public String generateRedirectUrl(long loginId, SocialType socialType) {
        return userReader.findBySocialTypeAndLoginId(socialType, loginId)
                .map(user -> {
                    // 가입된 사용자 -> 토큰 발급(로그인)
                    JwtDto jwtDto = jwtUtils.generateJwt(user);
                    tokenProcessor.save(jwtDto.getRefreshToken());
                    return clientAddress + "/auth"
                            + "?accessToken=" + jwtDto.getAccessToken()
                            + "&refreshToken=" + jwtDto.getRefreshToken();
                })
                .orElseGet(() -> {
                    // 새로운 사용자 -> 회원가입 폼으로
                    return clientAddress + "/join"
                            + "?socialType=" + socialType.getName()
                            + "&loginId=" + loginId;
                });
    }
}
