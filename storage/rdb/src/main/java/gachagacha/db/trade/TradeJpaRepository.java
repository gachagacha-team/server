package gachagacha.db.trade;

import gachagacha.domain.item.Item;
import gachagacha.domain.trade.TradeStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TradeJpaRepository extends JpaRepository<TradeEntity, Long> {

    Slice<TradeEntity> findBySellerId(long sellerId, Pageable pageable);

    List<TradeEntity> findBySellerId(long sellerId);

    List<TradeEntity> findByBuyerId(long buyerId);

    List<TradeEntity> findByItemAndTradeStatus(Item item, TradeStatus tradeStatus);

    @Modifying
    @Query("update TradeEntity t " +
            "set t.sellerId = -1 " +
            "where t.id = :tradeId")
    void softDeleteBySeller(@Param("tradeId") long tradeId);

    @Modifying
    @Query("update TradeEntity t " +
            "set t.buyerId = -1 " +
            "where t.id = :tradeId")
    void softDeleteByBuyer(@Param("tradeId") long tradeId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<TradeEntity> findFirstByItemAndTradeStatusAndSellerIdNotOrderByCreatedAtAsc(Item item, TradeStatus tradeStatus, long userId);
}
