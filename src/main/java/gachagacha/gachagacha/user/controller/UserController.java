package gachagacha.gachagacha.user.controller;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.user.dto.AttendanceResponse;
import gachagacha.gachagacha.user.dto.FollowRequest;
import gachagacha.gachagacha.user.dto.UnfollowRequest;
import gachagacha.gachagacha.user.service.UserService;
import gachagacha.gachagacha.user.dto.JoinRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public JwtDto join(@RequestBody JoinRequest joinRequest) {
        return userService.join(joinRequest);
    }

    @PostMapping("/attend")
    public AttendanceResponse attend(HttpServletRequest request) {
        return userService.attend(request);
    }

    @PostMapping("/follow")
    public void follow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        userService.follow(followRequest, request);
    }

    @DeleteMapping("/unfollow")
    public void unfollow(@RequestBody UnfollowRequest unfollowRequest, HttpServletRequest request) {
        userService.unfollow(unfollowRequest, request);
    }
}
