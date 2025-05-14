package gachagacha.domain.item_stock;

import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockRepository {

    void push(Long itemId, Long tradeId);

    Long popItemStock(long itemId);
}
