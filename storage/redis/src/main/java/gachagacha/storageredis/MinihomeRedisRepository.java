package gachagacha.storageredis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MinihomeRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String MINIHOME_VISITOR_COUNT_KEY = "minihome:visitor:count";

    public void increaseVisitorCount(Long minihomeId) {
        redisTemplate.opsForHash().increment(MINIHOME_VISITOR_COUNT_KEY, String.valueOf(minihomeId), 1);
    }

    public Map<Object, Object> getAllMinihomeIds() {
        return redisTemplate.opsForHash().entries(MINIHOME_VISITOR_COUNT_KEY);
    }
}
