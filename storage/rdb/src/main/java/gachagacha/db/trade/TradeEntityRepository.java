package gachagacha.db.trade;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.trade.Trade;
import gachagacha.domain.item.Item;
import gachagacha.domain.trade.TradeRepository;
import gachagacha.domain.trade.TradeStatus;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TradeEntityRepository implements TradeRepository {

    private final TradeJpaRepository tradeJpaRepository;

    @Override
    public Long save(Trade trade) {
        TradeEntity tradeEntity = tradeJpaRepository.save(TradeEntity.fromTrade(trade));
        return tradeEntity.getId();
    }

    @Override
    public void softDeleteBySeller(User seller) {
        tradeJpaRepository.findBySellerId(seller.getId())
                .stream()
                .forEach(tradeEntity -> tradeJpaRepository.softDeleteBySeller(tradeEntity.getId()));
    }

    @Override
    public void softDeleteByBuyer(User buyer) {
        tradeJpaRepository.findByBuyerId(buyer.getId())
                .stream()
                .forEach(tradeEntity -> tradeJpaRepository.softDeleteByBuyer(tradeEntity.getId()));
    }

    @Override
    public List<Trade> findOnSaleProductsByItem(Item item) {
        return tradeJpaRepository.findByItemAndTradeStatus(item, TradeStatus.ON_SALE)
                .stream()
                .filter(tradeEntity -> tradeEntity.getSellerId() != -1)
                .map(tradeEntity -> tradeEntity.toTrade())
                .toList();
    }

    @Override
    public Slice<Trade> findBySeller(User user, Pageable pageable) {
        return tradeJpaRepository.findBySellerId(user.getId(), pageable)
                .map(tradeEntity -> tradeEntity.toTrade());
    }

    @Override
    public void delete(Trade trade) {
        TradeEntity tradeEntity = tradeJpaRepository.findById(trade.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        tradeJpaRepository.delete(tradeEntity);
    }

    @Override
    public void update(Trade trade) {
        TradeEntity tradeEntity = tradeJpaRepository.findById(trade.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        tradeEntity.updateFromTrade(trade);
    }

    @Override
    public Optional<Trade> findById(long tradeId) {
        return tradeJpaRepository.findById(tradeId)
                .map(tradeEntity -> tradeEntity.toTrade());
    }

    @Override
    public Trade findFirstOnSaleProductWithLock(Item item) {
        return tradeJpaRepository.findFirstByItemAndTradeStatusAndSellerIdNotOrderByCreatedAtAsc(item, TradeStatus.ON_SALE, -1)
                        .map(tradeEntity -> tradeEntity.toTrade())
                                .orElseThrow(() -> new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT));
    }
}
