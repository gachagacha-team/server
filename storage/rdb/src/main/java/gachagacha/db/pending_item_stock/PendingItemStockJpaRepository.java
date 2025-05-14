package gachagacha.db.pending_item_stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingItemStockJpaRepository extends JpaRepository<PendingItemStockEntity, Long> {
}
