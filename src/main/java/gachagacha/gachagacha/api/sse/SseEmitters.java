package gachagacha.gachagacha.api.sse;

import gachagacha.gachagacha.domain.lotto.Lotto;
import gachagacha.gachagacha.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitters {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter put(String nickname, SseEmitter emitter) {
        this.emitters.put(nickname, emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void issuedLotto(Lotto lotto, User user) {
        SseEmitter sseEmitter = emitters.get(user.getNickname());
        if (sseEmitter == null) {
            log.info("SSE transmission failed: Could not find SSE emitter for user {}", user.getNickname());
        } else {
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("lotto")
                        .data(new LottoResponse(lotto.getId(), lotto.isWon(), lotto.getRewardCoin())));
                log.info("SSE transmission successful");
            } catch (IOException e) {
                log.error("SSE transmission failed: Error occurred during SSE transmission", e);
            }
        }
    }
}
