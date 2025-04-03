package gachagacha.gachagacha.api.sse;

import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitters sseEmitters;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        SseEmitter emitter = new SseEmitter(300000l);
        sseEmitters.put(user.getNickname(), emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }
}
