package gachagacha.gachagacha.trade.service;

import gachagacha.gachagacha.Trade;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.ItemType;
import gachagacha.gachagacha.item.repository.ItemRepository;
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

    private final TradeRepository tradeRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public AddTradeResponse addTrade(AddTradeRequest addTradeRequest, HttpServletRequest request) {
        Item item = itemRepository.findById(addTradeRequest.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        itemRepository.delete(item);

        String nickname = jwtUtils.getNicknameFromHeader(request);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        ItemType itemType = item.getItemType();
        Trade trade = Trade.create(itemType, addTradeRequest.getPrice());
        user.addTrade(trade);

        itemType.addTrade(addTradeRequest.getPrice());

        return new AddTradeResponse(itemType.getId(), trade.getPrice(), itemType.getAveragePrice());
    }
}
