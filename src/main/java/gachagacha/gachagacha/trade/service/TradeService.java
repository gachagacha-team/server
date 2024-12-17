package gachagacha.gachagacha.trade.service;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.trade.dto.AddTradeRequest;
import gachagacha.gachagacha.trade.dto.AddTradeResponse;
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

    public AddTradeResponse addTrade(AddTradeRequest addTradeRequest, HttpServletRequest request) {
        UserItem userItem = userItemRepository.findById(addTradeRequest.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        Item item = userItem.getItem();

        userItemRepository.delete(userItem);

        String nickname = jwtUtils.getNicknameFromHeader(request);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Trade trade = Trade.create(item, addTradeRequest.getPrice());
        user.addTrade(trade);
        item.addTrade(addTradeRequest.getPrice());

        return new AddTradeResponse(item.getItemId(), trade.getPrice(), item.getAveragePrice());
    }
}
