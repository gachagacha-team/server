package gachagacha.db.pending_item_stock;

import gachagacha.db.BaseEntity;
import gachagacha.domain.item_stock.PendingItemStock;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pending_item_stock")
@NoArgsConstructor
@AllArgsConstructor
public class PendingItemStockEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_item_stock_id")
    private Long id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Long tradeId;

    public static PendingItemStockEntity fromPendingItemStock(PendingItemStock pendingItemStock) {
        return new PendingItemStockEntity(pendingItemStock.getId(), pendingItemStock.getItemId(), pendingItemStock.getTradeId());
    }
}
