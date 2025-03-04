package gachagacha.gachagacha.domain.auth;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.CustomJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenProcessor {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX ="refresh_token:";

    public void save(String refreshToken) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + refreshToken, refreshToken, Duration.ofDays(30));
    }

    public void delete(String refreshToken) {
        validateRefreshToken(refreshToken);
        if (redisTemplate.hasKey(REFRESH_TOKEN_KEY_PREFIX + refreshToken)) {
            redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + refreshToken);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        if (redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + refreshToken) == null) {
            throw new CustomJwtException(ErrorCode.INVALID_JWT);
        }
    }
}
