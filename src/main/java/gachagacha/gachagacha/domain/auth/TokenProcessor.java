package gachagacha.gachagacha.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenProcessor {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX ="refresh_token:";

    public void saveRefreshToken(String newRefreshToken, String oldRefreshToken) {
        if (oldRefreshToken != null && redisTemplate.hasKey(REFRESH_TOKEN_KEY_PREFIX + oldRefreshToken)) {
            redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + oldRefreshToken);
        }
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + newRefreshToken, newRefreshToken, Duration.ofDays(30));
    }
}
