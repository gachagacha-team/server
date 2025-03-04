package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.*;
import gachagacha.gachagacha.domain.follow.Follow;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.domain.attendance.Attendance;
import gachagacha.gachagacha.domain.user.Coin;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.api_response.ApiResponse;
import gachagacha.gachagacha.domain.follow.FollowService;
import gachagacha.gachagacha.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final FollowService followService;

    @Value("${image.api.endpoints.profile}")
    private String profileImageApiEndpoint;

    @Operation(summary = "코인 조회")
    @GetMapping("/coin")
    public ApiResponse<CoinResponse> getCoin(HttpServletRequest request) {
        Coin coin = userService.getCoin(jwtUtils.getUserNicknameFromHeader(request));
        return ApiResponse.success(CoinResponse.of(coin));
    }

    @Operation(summary = "출석체크")
    @PostMapping("/attend")
    public ApiResponse<AttendanceResponse> attend(HttpServletRequest request) {
        String nickname = jwtUtils.getUserNicknameFromHeader(request);
        User user = userService.readUserByNickname(nickname);
        Attendance attendance = userService.attend(user);
        return ApiResponse.success(AttendanceResponse.of(attendance, user));
    }

    @Operation(summary = "팔로우")
    @PostMapping("/users/follow")
    public ApiResponse follow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        User followee = userService.readUserByNickname(followRequest.getFolloweeUserNickname());
        User follower = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        followService.follow(followee, follower);
        return ApiResponse.success();
    }

    @Operation(summary = "언팔로우")
    @DeleteMapping("/users/unfollow")
    public ApiResponse unfollow(@RequestBody UnfollowRequest unfollowRequest, HttpServletRequest request) {
        User followee = userService.readUserByNickname(unfollowRequest.getFolloweeUserNickname());
        User follower = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        followService.unfollow(followee, follower);
        return ApiResponse.success();
    }

    @Operation(summary = "사용자의 팔로워 리스트 조회(무한스크롤)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/users/{nickname}/followers")
    public ApiResponse<Slice<FollowerResponse>> getFollowers(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        Slice<Follow> followers = followService.getFollowers(nickname, pageable);
        User currentUser = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        User followee = userService.readUserByNickname(nickname);
        return ApiResponse.success(followers.map(follow -> {
            User follower = userService.readUserById(follow.getFollowerId());
            boolean isFollowing = followService.isFollowing(currentUser, follower);
            return FollowerResponse.of(follower, followee, currentUser, isFollowing, profileImageApiEndpoint);
        }));
    }

    @Operation(summary = "사용자의 팔로잉 리스트 조회(무한스크롤)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/users/{nickname}/followings")
    public ApiResponse<Slice<FollowingResponse>> getFollowings(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        Slice<Follow> followers = followService.getFollowings(nickname, pageable);
        User currentUser = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        return ApiResponse.success(followers.map(follow -> {
            User followee = userService.readUserById(follow.getFolloweeId());
            boolean isFollowing = followService.isFollowing(currentUser, followee);
            return FollowingResponse.of(followee, currentUser, isFollowing, profileImageApiEndpoint);
        }));
    }
}
