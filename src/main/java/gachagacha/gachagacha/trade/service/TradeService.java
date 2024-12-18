package gachagacha.gachagacha.trade.service;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.trade.dto.PurchaseResponse;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.trade.dto.AddTradeRequest;
import gachagacha.gachagacha.trade.dto.AddTradeResponse;
import gachagacha.gachagacha.trade.repository.TradeRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final TradeRepository tradeRepository;

    public AddTradeResponse addTrade(AddTradeRequest addTradeRequest, HttpServletRequest request) {
        UserItem userItem = userItemRepository.findById(addTradeRequest.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        Item item = userItem.getItem();

        userItemRepository.delete(userItem);

        String nickname = jwtUtils.getNicknameFromHeader(request);
        User seller = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Trade trade = Trade.create(seller, item, addTradeRequest.getPrice());
        item.addTrade(addTradeRequest.getPrice());
        tradeRepository.save(trade);

        return new AddTradeResponse(trade.getId(), trade.getSeller().getNickname(), item.getItemId(), trade.getPrice(), item.getAveragePrice(), trade.getStatus().getViewName());
    }

    public void cancelTrade(long tradeId, HttpServletRequest request) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TRADE));
        String nickname = jwtUtils.getNicknameFromHeader(request);
        validateAuthority(trade, nickname);

        trade.cancelTrade();
    }

    private void validateAuthority(Trade trade, String nickname) {
        if (!trade.getSeller().getNickname().equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    public PurchaseResponse purchase(long tradeId, HttpServletRequest request) {
        String nickname = jwtUtils.getNicknameFromHeader(request);
        User buyer = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TRADE));

        trade.processTrade(buyer);
        UserItem userItem = UserItem.create(trade.getItem());
        buyer.addItem(userItem);

        return new PurchaseResponse(trade.getId(), trade.getSeller().getNickname(), trade.getBuyer().getNickname(), trade.getItem().getViewName(), trade.getPrice(), trade.getStatus().getViewName());
    }
}
