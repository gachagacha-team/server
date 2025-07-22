package gachagacha.gachaapi.scheduler;

import gachagacha.db.pending_item_stock.PendingItemStockEntity;
import gachagacha.db.pending_item_stock.PendingItemStockJpaRepository;
import gachagacha.domain.item_stock.ItemStockProcessor;
import gachagacha.storageredis.TradeRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PendingItemStockScheduler {

    private final PendingItemStockJpaRepository pendingJpaRepository;
    private final TradeRedisRepository tradeRedisRepository;
    private final TransactionTemplate transactionTemplate;
    private final ItemStockProcessor itemStockProcessor;

//    @Scheduled(fixedRate = 5000)
    public void pushPendingItemStock() {
        for (PendingItemStockEntity pendingItemStockEntity : pendingJpaRepository.findAll()) {
            transactionTemplate.execute(status -> {
                itemStockProcessor.pushItemStock(pendingItemStockEntity.getItemId(), pendingItemStockEntity.getTradeId());
                pendingJpaRepository.delete(pendingItemStockEntity);
                return null;
            });
        }
    }
}
