package gachagacha.gachagacha.domain.trade;

import gachagacha.gachagacha.api.sse.SseEmitters;
import gachagacha.gachagacha.domain.decoration.DecorationProcessor;
import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItem;
import gachagacha.gachagacha.domain.notification.Notification;
import gachagacha.gachagacha.domain.notification.NotificationProcessor;
import gachagacha.gachagacha.domain.notification.NotificationType;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserReader;
import gachagacha.gachagacha.domain.user.UserUpdater;
import gachagacha.gachagacha.domain.item.UserItemAppender;
import gachagacha.gachagacha.domain.item.UserItemReader;
import gachagacha.gachagacha.domain.item.UserItemRemover;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeAppender tradeAppender;
    private final UserItemReader userItemReader;
    private final UserItemRemover userItemRemover;
    private final UserReader userReader;
    private final TradeReader tradeReader;
    private final UserItemAppender userItemAppender;
    private final TradeRemover tradeRemover;
    private final UserUpdater userUpdater;
    private final TradeUpdater tradeUpdater;
    private final DecorationProcessor decorationProcessor;
    private final SseEmitters sseEmitters;
    private final NotificationProcessor notificationProcessor;

    public List<Trade> readOnSaleProductsByItem(Item item) {
        return tradeReader.findOnSaleProductsByItem(item);
    }

    public Slice<Trade> readMyProducts(Pageable pageable, User user) {
        return tradeReader.findBySeller(user, pageable);
    }

    @Transactional
    public void registerTrade(User user, UserItem userItem) {
        validateUserItemAuthorization(user, userItem);
        if (decorationProcessor.isUsedInMinihomeDecoration(userItem, user)) {
            throw new BusinessException(ErrorCode.CANNOT_REGISTER_TRADE);
        }

        user.decreaseScoreForSaleItem(userItem.getItem(), userItemReader.findAllByUser(user));
        userItemRemover.delete(userItem);
        userUpdater.update(user);
        tradeAppender.save(Trade.of(user, userItem.getItem()));
    }

    private void validateUserItemAuthorization(User user, UserItem userItem) {
        if (userItem.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public void cancelTrade(User user, Trade trade) {
        validateTradeAuthorization(user, trade);

        user.increaseScoreByItem(trade.getItem(), userItemReader.findAllByUser(user));
        userItemAppender.addUserItem(UserItem.of(user, trade.getItem()));
        userUpdater.update(user);
        tradeRemover.delete(trade);
    }

    private void validateTradeAuthorization(User user, Trade trade) {
        if (trade.getSellerId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public void purchase(User buyer, Item item) {
        Trade trade = tradeReader.findOnSaleProductByItem(item);
        User seller = userReader.findById(trade.getSellerId());

        buyer.deductCoin(item.getItemGrade().getPrice());
        seller.addCoin(item.getItemGrade().getPrice());

        buyer.increaseScoreByItem(item, userItemReader.findAllByUser(buyer));
        trade.processTrade(buyer);

        userItemAppender.addUserItem(UserItem.of(buyer, item));
        userUpdater.update(buyer);
        userUpdater.update(seller);
        tradeUpdater.update(trade);

        Notification notification = Notification.of(NotificationType.TRADE_COMPLETED, new Notification.TradeCompletedNotification(item.getViewName(), item.getItemGrade().getPrice()));
        Long notificationId = notificationProcessor.saveNotification(notification, seller);
        sseEmitters.tradeComplete(seller, trade.getItem(), notificationId);
    }

    public Trade readById(long tradeId) {
        return tradeReader.findById(tradeId);
    }
}
