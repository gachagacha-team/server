package gachagacha.storageredis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TradeRedisRepository {

    private final RedisTemplate<String, Long> redisTemplate;
    private static final String ITEM_STOCK_PREFIX = "item:stock:";

    public void pushTradeId(Long itemId, Long tradeId) {
        redisTemplate.opsForList().rightPush(ITEM_STOCK_PREFIX + itemId, tradeId);
    }

    public Long getTradeId(Long itemId) {
        return redisTemplate.opsForList().leftPop(ITEM_STOCK_PREFIX + itemId);
    }
}
