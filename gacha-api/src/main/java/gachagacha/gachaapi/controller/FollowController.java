package gachagacha.gachaapi.controller;

import gachagacha.gachaapi.dto.request.FollowRequest;
import gachagacha.gachaapi.dto.request.UnfollowRequest;
import gachagacha.gachaapi.dto.response.FollowerResponse;
import gachagacha.gachaapi.dto.response.FollowingResponse;
import gachagacha.gachaapi.response.ApiResponse;
import gachagacha.gachaapi.jwt.JwtUtils;
import gachagacha.domain.follow.Follow;
import gachagacha.gachaapi.service.FollowService;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final FollowService followService;

    @Operation(summary = "팔로우")
    @PostMapping("/users/follow")
    public ApiResponse follow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        User followee = userService.readUserByNickname(followRequest.getFolloweeUserNickname());
        User follower = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        followService.follow(followee, follower);
        return ApiResponse.success();
    }

    @Operation(summary = "언팔로우")
    @DeleteMapping("/users/unfollow")
    public ApiResponse unfollow(@RequestBody UnfollowRequest unfollowRequest, HttpServletRequest request) {
        User followee = userService.readUserByNickname(unfollowRequest.getFolloweeUserNickname());
        User follower = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        followService.removeFollow(followee, follower);
        return ApiResponse.success();
    }

    @Operation(summary = "내 팔로워 삭제")
    @DeleteMapping("/users/follower/{nickname}")
    public ApiResponse removeFollower(@PathVariable String nickname, HttpServletRequest request) {
        User follower = userService.readUserByNickname(nickname);
        User followee = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        followService.removeFollow(followee, follower);
        return ApiResponse.success();
    }

    @Operation(summary = "사용자의 팔로워 리스트 조회(무한스크롤)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/users/{nickname}/followers")
    public ApiResponse<Slice<FollowerResponse>> getFollowers(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        Slice<Follow> followers = followService.getFollowers(nickname, pageable);
        User currentUser = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        User followee = userService.readUserByNickname(nickname);
        return ApiResponse.success(followers.map(follow -> {
            User follower = userService.readUserById(follow.getFollowerId());
            boolean isFollowing = followService.isFollowing(currentUser, follower);
            return FollowerResponse.of(follower, followee, currentUser, isFollowing);
        }));
    }

    @Operation(summary = "사용자의 팔로잉 리스트 조회(무한스크롤)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/users/{nickname}/followings")
    public ApiResponse<Slice<FollowingResponse>> getFollowings(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        Slice<Follow> followers = followService.getFollowings(nickname, pageable);
        User currentUser = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        return ApiResponse.success(followers.map(follow -> {
            User followee = userService.readUserById(follow.getFolloweeId());
            boolean isFollowing = followService.isFollowing(currentUser, followee);
            return FollowingResponse.of(followee, currentUser, isFollowing);
        }));
    }
}
