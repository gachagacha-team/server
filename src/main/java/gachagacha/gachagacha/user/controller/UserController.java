package gachagacha.gachagacha.user.controller;

import gachagacha.gachagacha.user.dto.*;
import gachagacha.gachagacha.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "코인 조회")
    @GetMapping("/coin")
    public CoinResponse getCoin(HttpServletRequest request) {
        return userService.getCoin(request);
    }

    @Operation(summary = "출석체크")
    @PostMapping("/attend")
    public AttendanceResponse attend(HttpServletRequest request) {
        return userService.attend(request);
    }

    @Operation(summary = "팔로우")
    @PostMapping("/users/follow")
    public void follow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        userService.follow(followRequest, request);
    }

    @Operation(summary = "언팔로우")
    @DeleteMapping("/users/unfollow")
    public void unfollow(@RequestBody UnfollowRequest unfollowRequest, HttpServletRequest request) {
        userService.unfollow(unfollowRequest, request);
    }

    @Operation(summary = "사용자의 팔로워 리스트 조회(무한스크롤)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/users/{nickname}/followers")
    public Slice<FollowerResponse> getFollowers(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        return userService.getFollowers(nickname, request, pageable);
    }

    @Operation(summary = "사용자의 팔로잉 리스트 조회(무한스크롤)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/users/{nickname}/followings")
    public Slice<FollowingResponse> getFollowings(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        return userService.getFollowings(nickname, request, pageable);
    }
}
