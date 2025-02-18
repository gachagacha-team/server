package gachagacha.gachagacha.trade.repository;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.trade.entity.TradeStatus;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByItem(Item item);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Trade> findFirstByItemAndTradeStatusOrderByCreatedAtAsc(Item item, TradeStatus tradeStatus);

    Slice<Trade> findBySeller(User seller, Pageable pageable);
}
