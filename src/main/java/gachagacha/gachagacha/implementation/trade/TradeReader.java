package gachagacha.gachagacha.implementation.trade;

import gachagacha.gachagacha.repository.TradeRepository;
import gachagacha.gachagacha.domain.Item;
import gachagacha.gachagacha.domain.Trade;
import gachagacha.gachagacha.domain.TradeStatus;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TradeReader {

    private final TradeRepository tradeRepository;

    public Trade findById(long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT))
                .toTrade();
    }

    public Slice<Trade> findBySeller(User user, Pageable pageable) {
        return tradeRepository.findBySellerId(user.getId(), pageable)
                .map(tradeEntity -> tradeEntity.toTrade());

    }

    public Trade findOnSaleProductByItem(Item item) {
        return tradeRepository.findFirstByItemAndTradeStatusAndSellerIdNotOrderByCreatedAtAsc(item, TradeStatus.ON_SALE, -1)
                .orElseThrow(() -> new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT))
                .toTrade();
    }

    public List<Trade> findOnSaleProductsByItem(Item item) {
        return tradeRepository.findByItemAndTradeStatus(item, TradeStatus.ON_SALE)
                .stream()
                .filter(tradeEntity -> tradeEntity.getSellerId() != -1)
                .map(tradeEntity -> tradeEntity.toTrade())
                .toList();
    }
}
