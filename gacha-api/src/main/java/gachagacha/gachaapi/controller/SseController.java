package gachagacha.gachaapi.controller;

import gachagacha.gachaapi.auth.jwt.JwtUtils;
import gachagacha.gachaapi.service.SseService;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        SseEmitter emitter = new SseEmitter(300000l);
        sseService.put(user.getId(), emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @PostMapping(value = "/sse/test")
    public String sseTest(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        sseService.test(user);
        return "success!";
    }
}
