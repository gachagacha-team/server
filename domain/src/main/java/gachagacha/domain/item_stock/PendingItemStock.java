package gachagacha.domain.item_stock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PendingItemStock {

    private Long id;
    private Long itemId;
    private Long tradeId;
}
