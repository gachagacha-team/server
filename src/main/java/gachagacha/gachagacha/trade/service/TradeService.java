package gachagacha.gachagacha.trade.service;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.trade.dto.*;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.trade.entity.TradeStatus;
import gachagacha.gachagacha.trade.repository.TradeRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    public Slice<ReadTradesResponse> readOnSaleTrades(Pageable pageable) {
        return  tradeRepository.findByOnSale(TradeStatus.ON_SALE, pageable)
                .map(trade -> new ReadTradesResponse(trade.getId(), trade.getItem().getViewName(),
                        "/image" + trade.getItem().getFilePath(), trade.getItem().getGrade().name(), trade.getPrice(), trade.getStatus().getViewName()));
    }

    public Slice<ReadTradesResponse> readSoldTrades(Pageable pageable) {
        return  tradeRepository.findByOnSale(TradeStatus.COMPLETED, pageable)
                .map(trade -> new ReadTradesResponse(trade.getId(), trade.getItem().getViewName(),
                        "/image" + trade.getItem().getFilePath(), trade.getItem().getGrade().name(), trade.getPrice(), trade.getStatus().getViewName()));
    }

    public Slice<ReadTradesResponse> readMyProductTrades(Pageable pageable, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return tradeRepository.findByUser(seller, pageable)
                .map(trade -> new ReadTradesResponse(trade.getId(), trade.getItem().getViewName(),
                        "/image" + trade.getItem().getFilePath(), trade.getItem().getGrade().getViewName(), trade.getPrice(), trade.getStatus().getViewName()));
    }

    public ReadTradeResponse readTrade(long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TRADE));
        if (trade.getStatus() == TradeStatus.COMPLETED) {
            return ReadTradeResponse.fromSold(trade.getItem().getViewName(),"/image" + trade.getItem().getFilePath(),
                    trade.getItem().getGrade().getViewName(), trade.getPrice(), trade.getStatus().getViewName(), trade.getSeller().getNickname(),
                    trade.getTransactionDate());
        }
        return ReadTradeResponse.fromOnSaleOrCancelled(trade.getItem().getViewName(),"/image" + trade.getItem().getFilePath(),
                trade.getItem().getGrade().getViewName(), trade.getPrice(), trade.getStatus().getViewName(), trade.getSeller().getNickname());
    }

    public ReadTradeResponse editTrade(long tradeId, EditTradeRequest editTradeRequest) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TRADE));
        if (trade.getStatus() == TradeStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.CANNOT_EDIT_COMPLETED_TRADE);
        }

        trade.edit(editTradeRequest.getPrice(), TradeStatus.findByViewName(editTradeRequest.getStatus()));

        return ReadTradeResponse.fromOnSaleOrCancelled(trade.getItem().getViewName(),"/image" + trade.getItem().getFilePath(),
                trade.getItem().getGrade().getViewName(), trade.getPrice(), trade.getStatus().getViewName(), trade.getSeller().getNickname());
    }

    public ReadItemTradeResponse readItemTrade(long itemId) {
        Item item = Item.findById(itemId);
        return new ReadItemTradeResponse(item.getViewName(), item.getGrade().getViewName(), item.getAveragePrice());
    }

    public AddTradeResponse addTrade(AddTradeRequest addTradeRequest, HttpServletRequest request) {
        UserItem userItem = userItemRepository.findById(addTradeRequest.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        Item item = userItem.getItem();

        userItemRepository.delete(userItem);

        long userId = jwtUtils.getUserIdFromHeader(request);
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Trade trade = Trade.create(seller, item, addTradeRequest.getPrice());
        item.addTrade(addTradeRequest.getPrice());
        tradeRepository.save(trade);

        return new AddTradeResponse(trade.getId(), trade.getSeller().getNickname(), item.getItemId(), trade.getPrice(), item.getAveragePrice(), trade.getStatus().getViewName());
    }

    public BuyItemResponse buy(long tradeId, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TRADE));

        trade.processTrade(buyer);
        UserItem userItem = UserItem.create(trade.getItem());
        buyer.addItem(userItem);

        return new BuyItemResponse(trade.getId(), trade.getSeller().getNickname(), trade.getBuyer().getNickname(), trade.getItem().getViewName(), trade.getPrice(), trade.getStatus().getViewName());
    }
}
