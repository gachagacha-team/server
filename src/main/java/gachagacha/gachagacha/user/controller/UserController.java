package gachagacha.gachagacha.user.controller;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.user.dto.*;
import gachagacha.gachagacha.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/attend")
    public AttendanceResponse attend(HttpServletRequest request) {
        return userService.attend(request);
    }

    @PostMapping("/users/follow")
    public void follow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        userService.follow(followRequest, request);
    }

    @DeleteMapping("/users/unfollow")
    public void unfollow(@RequestBody UnfollowRequest unfollowRequest, HttpServletRequest request) {
        userService.unfollow(unfollowRequest, request);
    }

    @GetMapping("/users/{nickname}/followers")
    public Slice<FollowerResponse> getFollowers(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        return userService.getFollowers(nickname, request, pageable);
    }

    @GetMapping("/users/{nickname}/followings")
    public Slice<FollowingResponse> getFollowings(@PathVariable String nickname, HttpServletRequest request, Pageable pageable) {
        return userService.getFollowings(nickname, request, pageable);
    }
}
