package gachagacha.gachagacha.trade.repository;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByItem(Item item);

    List<Trade> findByItemOrderByCreatedAtAsc(Item item);

    Slice<Trade> findBySeller(User seller, Pageable pageable);
}
