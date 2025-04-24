package gachagacha.storageredis;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.CustomJwtException;
import gachagacha.domain.auth.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenRedisRepository implements TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX ="refresh_token:";

    @Override
    public void save(String refreshToken) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + refreshToken, refreshToken, Duration.ofDays(30));
    }

    @Override
    public void delete(String refreshToken) {
        validateRefreshToken(refreshToken);
        if (redisTemplate.hasKey(REFRESH_TOKEN_KEY_PREFIX + refreshToken)) {
            redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + refreshToken);
        }
    }

    @Override
    public void validateRefreshToken(String refreshToken) {
        if (redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + refreshToken) == null) {
            throw new CustomJwtException(ErrorCode.INVALID_JWT);
        }
    }
}
