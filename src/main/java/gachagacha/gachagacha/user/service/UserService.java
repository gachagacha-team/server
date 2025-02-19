package gachagacha.gachagacha.user.service;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.user.dto.*;
import gachagacha.gachagacha.user.entity.Attendance;
import gachagacha.gachagacha.user.entity.Follow;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.AttendanceRepository;
import gachagacha.gachagacha.user.repository.FollowRepository;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${image.api.endpoints.profile}")
    private String profileImageApiEndpoint;

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final FollowRepository followRepository;
    private final JwtUtils jwtUtils;

    public CoinResponse getCoin(HttpServletRequest request) {
        User user = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return new CoinResponse(user.getCoinAmount());
    }

    @Transactional
    public AttendanceResponse attend(HttpServletRequest request) {
        User user = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateDuplicatedAttendance(user);
        int bonusCoin = (new Random().nextInt(5) + 1) * 1000;
        user.addCoinByAttendance(bonusCoin);
        attendanceRepository.save(Attendance.create(user, LocalDate.now()));
        return new AttendanceResponse(bonusCoin, user.getCoinAmount());
    }

    private void validateDuplicatedAttendance(User user) {
        LocalDate date = LocalDate.now();
        if (attendanceRepository.findByUserAndDate(user, date).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_ATTEND);
        }
    }

    @Transactional
    public void follow(FollowRequest followRequest, HttpServletRequest request) {
        log.info("dto = {}", followRequest.getFolloweeUserNickname());
        User follower = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        log.info("follower = {}", follower.getNickname());
        User followee = userRepository.findByNickname(followRequest.getFolloweeUserNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        log.info("followee = {}", followee.getNickname());


        validateSelfFollow(follower, followee);
        validateDuplicatedFollow(follower, followee);

        Follow follow = Follow.create(follower, followee);
        followRepository.save(follow);
    }

    private void validateSelfFollow(User follower, User followee) {
        if (follower.getId() == followee.getId()) {
            throw new BusinessException(ErrorCode.CANNOT_SELF_FOLLOW);
        }
    }

    private void validateDuplicatedFollow(User follower, User followee) {
        if (followRepository.findByFollowerAndFollowee(follower, followee).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWING);
        }
    }

    @Transactional
    public void unfollow(UnfollowRequest unfollowRequest, HttpServletRequest request) {
        User follower = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User followee = userRepository.findByNickname(unfollowRequest.getFolloweeUserNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FOLLOW));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public Slice<FollowerResponse> getFollowers(String nickname, HttpServletRequest request, Pageable pageable) {
        User currentUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return followRepository.findByFollowee(minihomeUser, pageable)
                .map(follow -> {
                    User follower = follow.getFollower();
                    boolean isFollowing = followRepository.findByFollowerAndFollowee(currentUser, follower).isPresent();
                    return new FollowerResponse(follower.getId(), follower.getNickname(), profileImageApiEndpoint + follower.getProfileImage().getStoreFileName(),
                            isFollowing, currentUser.getId() == minihomeUser.getId(), currentUser.getId() == follower.getId());
                });
    }

    @Transactional(readOnly = true)
    public Slice<FollowingResponse> getFollowings(String nickname, HttpServletRequest request, Pageable pageable) {
        User currentUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return followRepository.findByFollower(minihomeUser, pageable)
                .map(follow -> {
                    User followee = follow.getFollowee();
                    boolean isFollowing = followRepository.findByFollowerAndFollowee(currentUser, followee).isPresent();
                    return new FollowingResponse(followee.getId(), followee.getNickname(), profileImageApiEndpoint + followee.getProfileImage().getStoreFileName(),
                            isFollowing, currentUser.getId() == followee.getId());
                });
    }
}
