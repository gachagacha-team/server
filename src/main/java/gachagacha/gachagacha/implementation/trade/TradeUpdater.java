package gachagacha.gachagacha.implementation.trade;

import gachagacha.gachagacha.repository.TradeRepository;
import gachagacha.gachagacha.domain.Trade;
import gachagacha.gachagacha.entity.TradeEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeUpdater {

    private final TradeRepository tradeRepository;

    public void update(Trade trade) {
        TradeEntity tradeEntity = tradeRepository.findById(trade.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        tradeEntity.updateFromTrade(trade);
    }
}
