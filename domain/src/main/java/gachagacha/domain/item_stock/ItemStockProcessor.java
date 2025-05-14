package gachagacha.domain.item_stock;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.item.Item;
import gachagacha.domain.trade.Trade;
import gachagacha.domain.trade.TradeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStockProcessor {

    private final ItemStockRepository itemStockRepository;
    private final PendingItemStockRepository pendingItemStockRepository;
    private final TradeRepository tradeRepository;

    @CircuitBreaker(name = "redis", fallbackMethod = "savePendingItemStock")
    public void registerTradeToRedis(Long itemId, Long tradeId) { // 아이템 등록시 -> 실패하면 pending 저장
        itemStockRepository.push(itemId, tradeId);
    }

    /** fallback method */
    private void savePendingItemStock(Long itemId, Long tradeId, Exception e) {
        log.info("Redis circuit breaker open! exception = {}", e);
        pendingItemStockRepository.save(new PendingItemStock(null, itemId, tradeId));
    }

    public void pushItemStock(Long itemId, Long tradeId) { // 아이템 등록시 -> 실패하면 pending 저장
        itemStockRepository.push(itemId, tradeId);
    }

    @CircuitBreaker(name = "redis", fallbackMethod = "purchaseWithRdb")
    public Trade getTrade(Item item) {
        Long tradeId = itemStockRepository.popItemStock(item.getItemId());
        if (tradeId == null) { // 재고 없는 경우 -> 예외 응답 반환
            throw new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT);
        }

        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    /** fallback method */
    private Trade purchaseWithRdb(Item item, Exception e) {
        if (e instanceof BusinessException) {
            throw (BusinessException)e;
        }
        // 레디스 장애시 -> RDB에서 거래
        return tradeRepository.findFirstOnSaleProductWithLock(item);
    }
}
