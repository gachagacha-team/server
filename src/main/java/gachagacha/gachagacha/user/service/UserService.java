package gachagacha.gachagacha.user.service;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.user.dto.*;
import gachagacha.gachagacha.user.entity.Attendance;
import gachagacha.gachagacha.user.entity.Follow;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.AttendanceRepository;
import gachagacha.gachagacha.user.repository.FollowRepository;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final FollowRepository followRepository;
    private final JwtUtils jwtUtils;

    public JwtDto join(JoinRequest joinRequest) {
        LoginType loginType = LoginType.find(joinRequest.getLoginType());

        validateDuplicatedUser(loginType, joinRequest.getLoginId());
        validateDuplicatedNickname(joinRequest.getNickname());

        User user = User.create(loginType, joinRequest.getLoginId(), joinRequest.getNickname(), Minihome.create(), joinRequest.getProfileUrl());
        userRepository.save(user);
        return jwtUtils.generateJwt(user.getId());
    }

    private void validateDuplicatedUser(LoginType loginType, long loginId) {
        if (userRepository.findByLoginTypeAndLoginId(loginType, loginId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_REGISTRATION);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    public AttendanceResponse attend(HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateDuplicatedAttendance(user);
        int bonusCoin = user.attend();
        Attendance attendance = Attendance.create(user, LocalDate.now());
        attendanceRepository.save(attendance);
        return new AttendanceResponse(bonusCoin, user.getCoin());
    }

    private void validateDuplicatedAttendance(User user) {
        LocalDate date = LocalDate.now();
        if (attendanceRepository.findByUserAndDate(user, date).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_ATTEND);
        }
    }

    public void follow(FollowRequest followRequest, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User followee = userRepository.findById(followRequest.getFolloweeUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

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

    public void unfollow(UnfollowRequest unfollowRequest, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User followee = userRepository.findById(unfollowRequest.getFolloweeUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FOLLOW));

        followRepository.delete(follow);
    }

    public Slice<FollowerResponse> getFollowers(String nickname, HttpServletRequest request, Pageable pageable) {
        User currentUser = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return followRepository.findByFollowee(minihomeUser, pageable)
                .map(follow -> {
                    User follower = follow.getFollower();
                    boolean isFollowing = followRepository.findByFollowerAndFollowee(currentUser, follower).isPresent();
                    return new FollowerResponse(follower.getId(), follower.getNickname(), follower.getProfileImageUrl(),
                            isFollowing, currentUser.getId() == minihomeUser.getId(), currentUser.getId() == follower.getId());
                });
    }

    public Slice<FollowingResponse> getFollowings(String nickname, HttpServletRequest request, Pageable pageable) {
        User currentUser = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return followRepository.findByFollower(minihomeUser, pageable)
                .map(follow -> {
                    User followee = follow.getFollowee();
                    boolean isFollowing = followRepository.findByFollowerAndFollowee(currentUser, followee).isPresent();
                    return new FollowingResponse(followee.getId(), followee.getNickname(), followee.getProfileImageUrl(),
                            isFollowing, currentUser.getId() == followee.getId());
                });
    }
}
