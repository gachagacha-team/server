package gachagacha.gachagacha.user.controller;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.user.dto.AttendanceResponse;
import gachagacha.gachagacha.user.dto.FollowRequest;
import gachagacha.gachagacha.user.service.UserService;
import gachagacha.gachagacha.user.dto.JoinRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
