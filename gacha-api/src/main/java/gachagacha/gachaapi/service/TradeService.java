package gachagacha.gachaapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.decoration.DecorationRepository;
import gachagacha.domain.item.*;
import gachagacha.domain.lotto.LottoIssuanceEvent;
import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationRepository;
import gachagacha.domain.notification.NotificationType;
import gachagacha.domain.outbox.Outbox;
import gachagacha.domain.outbox.OutboxRepository;
import gachagacha.domain.trade.Trade;
import gachagacha.domain.trade.TradeRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final NotificationRepository notificationRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final DecorationRepository decorationRepository;
    private final TradeRepository tradeRepository;

    @Value("${spring.data.redis.stream.lotto-issuance-requests}")
    private String topic;

    public List<Trade> readOnSaleProductsByItem(Item item) {
        return tradeRepository.findOnSaleProductsByItem(item);
    }

    public Slice<Trade> readMyProducts(Pageable pageable, User user) {
        return tradeRepository.findBySeller(user, pageable);
    }

    @Transactional
    public void registerTrade(User user, UserItem userItem) {
        validateUserItemAuthorization(user, userItem);
        if (decorationRepository.isUsedInMinihomeDecoration(userItem, user)) {
            throw new BusinessException(ErrorCode.CANNOT_REGISTER_TRADE);
        }

        user.decreaseScoreForSaleItem(userItem.getItem(), userItemRepository.findByUser(user));
        userItemRepository.delete(userItem);
        userRepository.update(user);
        tradeRepository.save(Trade.of(user, userItem.getItem()));
    }

    private void validateUserItemAuthorization(User user, UserItem userItem) {
        if (userItem.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public void cancelTrade(User user, Trade trade) {
        validateTradeAuthorization(user, trade);

        user.increaseScoreByItem(trade.getItem(), userItemRepository.findByUser(user));
        userItemRepository.save(UserItem.of(user, trade.getItem()));
        userRepository.update(user);
        tradeRepository.delete(trade);
    }

    private void validateTradeAuthorization(User user, Trade trade) {
        if (trade.getSellerId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public Notification purchase(User buyer, Item item) throws JsonProcessingException {
        Trade trade = tradeRepository.findFirstProduct(item);
        User seller = userRepository.findById(trade.getSellerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        buyer.deductCoin(item.getItemGrade().getPrice());
        seller.addCoin(item.getItemGrade().getPrice());

        buyer.increaseScoreByItem(item, userItemRepository.findByUser(buyer));
        trade.processTrade(buyer);

        userItemRepository.save(UserItem.of(buyer, item));
        userRepository.update(buyer);
        userRepository.update(seller);
        tradeRepository.update(trade);

        String notificationMessage = NotificationType.TRADE_COMPLETED.generateNotificationMessageByTradeCompleted(item);
        Notification notification = Notification.of(seller.getId(), notificationMessage, NotificationType.TRADE_COMPLETED);
        Long notificationId = notificationRepository.saveNotification(notification);
        Notification savedNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_NOTIFICATION));

        LottoIssuanceEvent lottoIssuanceEvent = new LottoIssuanceEvent(buyer.getId(), item.getItemGrade());
        String payload = objectMapper.writeValueAsString(lottoIssuanceEvent);
        outboxRepository.save(Outbox.create(topic, payload));

        return savedNotification;
    }

    public Trade readById(long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
    }
}
