package gachagacha.domain.trade;

import gachagacha.domain.item.Item;
import gachagacha.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository {

    Long save(Trade trade);

    void softDeleteBySeller(User seller);

    void softDeleteByBuyer(User buyer);

    List<Trade> findOnSaleProductsByItem(Item item);

    Slice<Trade> findBySeller(User user, Pageable pageable);

    void delete(Trade trade);

    void update(Trade trade);

    Optional<Trade> findById(long tradeId);
}
