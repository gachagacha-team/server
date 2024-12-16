package gachagacha.gachagacha.user.controller;

import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.user.service.UserService;
import gachagacha.gachagacha.user.dto.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public HttpEntity join(@RequestBody JoinRequest joinRequest) {
        JwtDto jwtDto = userService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(jwtDto);
    }
}
