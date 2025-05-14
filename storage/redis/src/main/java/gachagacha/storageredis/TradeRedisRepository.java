package gachagacha.storageredis;

import gachagacha.domain.item_stock.ItemStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TradeRedisRepository implements ItemStockRepository {

    private final RedisTemplate<String, Long> redisTemplate;
    private static final String ITEM_STOCK_PREFIX = "item:stock:";

    @Override
    public void push(Long itemId, Long tradeId) {
        redisTemplate.opsForList().rightPush(ITEM_STOCK_PREFIX + itemId, tradeId);
    }

    @Override
    public Long popItemStock(long itemId) {
        return redisTemplate.opsForList().leftPop(ITEM_STOCK_PREFIX + itemId);
    }
}
