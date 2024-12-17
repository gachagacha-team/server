package gachagacha.gachagacha.trade.repository;

import gachagacha.gachagacha.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
}
