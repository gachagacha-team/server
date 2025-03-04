package gachagacha.gachagacha.domain.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TradeAppender {

    private final TradeRepository tradeRepository;

    @Transactional
    public void save(Trade trade) {
        tradeRepository.save(trade.toTradeEntity());
    }
}
