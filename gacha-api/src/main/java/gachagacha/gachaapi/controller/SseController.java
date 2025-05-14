package gachagacha.gachaapi.controller;

import gachagacha.domain.item.Item;
import gachagacha.domain.item.ItemGrade;
import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationType;
import gachagacha.gachaapi.auth.jwt.JwtUtils;
import gachagacha.gachaapi.notification.SseProcessor;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseProcessor sseProcessor;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        SseEmitter emitter = new SseEmitter(300000l);
        sseProcessor.put(user.getId(), emitter);
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
        sseProcessor.issuedLotto(new Notification(1l, user.getId(),
                NotificationType.LOTTO_ISSUED.generateNotificationMessageByLottoIssued(ItemGrade.S),
                NotificationType.LOTTO_ISSUED));
        return "success!";
    }

    @PostMapping(value = "/sse/test2")
    public String sseTest2(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        sseProcessor.tradeComplete(new Notification(1l, user.getId(),
                NotificationType.TRADE_COMPLETED.generateNotificationMessageByTradeCompleted(Item.BLACK_CAT),
                NotificationType.TRADE_COMPLETED));
        return "success!";
    }
}
