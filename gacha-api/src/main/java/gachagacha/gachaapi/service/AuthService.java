package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.attendance.AttendanceRepository;
import gachagacha.domain.user.*;
import gachagacha.gachaapi.auth.jwt.Jwt;
import gachagacha.gachaapi.auth.jwt.JwtUtils;
import gachagacha.domain.auth.TokenRepository;
import gachagacha.domain.decoration.DecorationRepository;
import gachagacha.domain.follow.FollowRepository;
import gachagacha.domain.guestbook.GuestbookRepository;
import gachagacha.domain.item.UserItemRepository;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.domain.trade.TradeRepository;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final TokenRepository tokenRepository;
    private final DecorationRepository decorationRepository;
    private final UserRepository userRepository;
    private final MinihomeRepository minihomeRepository;
    private final GuestbookRepository guestbookRepository;
    private final AttendanceRepository attendanceRepository;
    private final FollowRepository followRepository;
    private final UserItemRepository userItemRepository;
    private final TradeRepository tradeRepository;
    private final UserBackgroundRepository userBackgroundRepository;

    @Transactional
    public Jwt join(String nickname, SocialType socialType, Long loginId, Profile profile) {
        validateDuplicatedUser(socialType, loginId);
        validateDuplicatedNickname(nickname);

        long userId = userRepository.save(User.createInitialUser(socialType, loginId, nickname, profile));
        minihomeRepository.save(new Minihome(null, userId, 0, 0));

        userBackgroundRepository.saveBasicBackgrounds(userId);

        Decoration decoration = new Decoration(userId, Background.WHITE, new ArrayList<>());
        decorationRepository.save(decoration, userId);

        Jwt jwt = jwtUtils.generateJwt(userId, profile.getId());
        tokenRepository.save(jwt.getRefreshToken());
        return jwt;
    }

    private void validateDuplicatedUser(SocialType socialType, long loginId) {
        if (userRepository.existsBySocialTypeAndLoginId(socialType, loginId)) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_REGISTRATION);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    public Jwt renewTokens(String refreshToken, Long userId) {
        tokenRepository.delete(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Jwt jwt = jwtUtils.generateJwt(user.getId(), user.getProfile().getId());
        tokenRepository.save(jwt.getRefreshToken());
        return jwt;
    }

    public void logout(String refreshToken) {
        tokenRepository.delete(refreshToken);
    }

    @Transactional
    public void withdraw(User user, String refreshToken) {
        Minihome minihome = minihomeRepository.findByUserId(user.getId());

        // trade 엔티티 soft delete
        tradeRepository.softDeleteBySeller(user);
        tradeRepository.softDeleteByBuyer(user);

        // guestbook 엔티티 soft delete
        guestbookRepository.softDeleteByMinihome(minihome);
        guestbookRepository.softDeleteByAuthor(user);

        // follow 엔티티 삭제
        followRepository.deleteAllByFolloweeId(user.getId());
        followRepository.deleteAllByFollowerId(user.getId());


        // RT 삭제
        tokenRepository.delete(refreshToken);

        // userItem, minihome, attendance, user 엔티티 삭제
        userItemRepository.deleteAllByUserId(user.getId());
        minihomeRepository.delete(minihome);
        attendanceRepository.deleteAllByUserId(user.getId());
        userRepository.delete(user);
    }

    public Optional<User> findUser(long loginId, SocialType socialType) {
        return userRepository.findBySocialTypeAndLoginId(socialType, loginId);
    }

    public void saveRefreshToken(String refreshToken) {
        tokenRepository.save(refreshToken);
    }
}
