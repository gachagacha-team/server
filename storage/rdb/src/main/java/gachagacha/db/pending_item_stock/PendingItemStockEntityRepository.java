package gachagacha.db.pending_item_stock;

import gachagacha.domain.item_stock.PendingItemStock;
import gachagacha.domain.item_stock.PendingItemStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PendingItemStockEntityRepository implements PendingItemStockRepository {

    private final PendingItemStockJpaRepository pendingItemStockJpaRepository;

    @Override
    public Long save(PendingItemStock pendingItemStock) {
        PendingItemStockEntity savedEntity = pendingItemStockJpaRepository.save(PendingItemStockEntity.fromPendingItemStock(pendingItemStock));
        return savedEntity.getId();
    }
}
