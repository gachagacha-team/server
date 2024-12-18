package gachagacha.gachagacha.trade.repository;

import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.trade.entity.TradeStatus;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("select t from Trade t " +
            "where t.status = :status")
    Slice<Trade> findByOnSale(@Param("status") TradeStatus status, Pageable pageable);

    @Query("select t from Trade t " +
            "where t.seller = :user")
    Slice<Trade> findByUser(User user, Pageable pageable);
}
