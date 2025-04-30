package gachagacha.gachaapi.service;

import gachagacha.domain.user.User;
import gachagacha.gachaapi.dto.response.NotificationsResponse;
import gachagacha.domain.notification.Notification;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter put(Long userId, SseEmitter emitter) {
        this.emitters.put(userId, emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
            log.info("emitter list size: {}", emitters.size());

        });

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(userId);
            log.info("emitter list size: {}", emitters.size());

        });

        return emitter;
    }

    public void issuedLotto(Notification notification) {
        SseEmitter sseEmitter = emitters.get(notification.getUserId());
        if (sseEmitter == null) {
            log.info("SSE transmission failed: Could not find SSE emitter for user");
        } else {
            try {
                NotificationsResponse.NotificationDto notificationDto = new NotificationsResponse.NotificationDto(
                        notification.getId(),
                        notification.getMessage(),
                        notification.getNotificationType().getViewName()
                        );
                sseEmitter.send(SseEmitter.event()
                        .name("lotto_issued")
                        .data(notificationDto)
                );
                log.info("SSE transmission successful");
            } catch (IOException e) {
                log.error("SSE transmission failed: Error occurred during SSE transmission", e);
            }
        }
    }

    public void tradeComplete(Notification notification) {
        SseEmitter sseEmitter = emitters.get(notification.getUserId());
        if (sseEmitter == null) {
            log.info("SSE transmission failed: Could not find SSE emitter");
        } else {
            try {
                NotificationsResponse.NotificationDto notificationDto = new NotificationsResponse.NotificationDto(
                        notification.getId(),
                        notification.getMessage(),
                        notification.getNotificationType().getViewName()
                );
                sseEmitter.send(SseEmitter.event()
                        .name("trade_completed")
                        .data(notificationDto)
                );
                log.info("SSE transmission successful");
            } catch (IOException e) {
                log.error("SSE transmission failed: Error occurred during SSE transmission", e);
            }
        }
    }

    public void test(User user) {
        SseEmitter sseEmitter = emitters.get(user.getId());
        if (sseEmitter == null) {
            log.info("SSE transmission failed: Could not find SSE emitter");
        } else {
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("sold item")
                        .data("알림 도착~~!!")
                );
                log.info("SSE transmission successful");
            } catch (IOException e) {
                log.error("SSE transmission failed: Error occurred during SSE transmission", e);
            }
        }
    }
}
