package gachagacha.gachagacha.domain.trade;

import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeRemover {

    private final TradeRepository tradeRepository;

    public void delete(Trade trade) {
        tradeRepository.delete(trade.toTradeEntity());
    }

    public void softDeleteBySeller(User user) {
        tradeRepository.findBySellerId(user.getId())
                .stream()
                .forEach(tradeEntity -> tradeRepository.softDeleteBySeller(tradeEntity.getId()));
    }

    public void softDeleteByBuyer(User user) {
        tradeRepository.findByBuyerId(user.getId())
                .stream()
                .forEach(tradeEntity -> tradeRepository.softDeleteByBuyer(tradeEntity.getId()));
    }
}
