package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.request.FollowRequest;
import gachagacha.gachagacha.api.dto.request.UnfollowRequest;
import gachagacha.gachagacha.api.dto.response.FollowerResponse;
import gachagacha.gachagacha.api.dto.response.FollowingResponse;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.follow.Follow;
import gachagacha.gachagacha.domain.follow.FollowService;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.jwt.JwtUtils;
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
public class FollowController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final FollowService followService;

    @Value("${image.api.endpoints.profile}")
    private String profileImageApiEndpoint;

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
        followService.removeFollow(followee, follower);
        return ApiResponse.success();
    }

    @Operation(summary = "내 팔로워 삭제")
    @DeleteMapping("/users/follower/{nickname}")
    public ApiResponse removeFollower(@PathVariable String nickname, HttpServletRequest request) {
        User follower = userService.readUserByNickname(nickname);
        User followee = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        followService.removeFollow(followee, follower);
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
