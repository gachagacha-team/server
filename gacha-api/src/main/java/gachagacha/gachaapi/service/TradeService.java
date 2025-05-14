package gachagacha.gachaapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.db.pending_item_stock.PendingItemStockEntity;
import gachagacha.db.pending_item_stock.PendingJpaRepository;
import gachagacha.domain.decoration.DecorationRepository;
import gachagacha.domain.item.*;
import gachagacha.domain.lotto.LottoIssuanceEvent;
import gachagacha.domain.notification.Notification;
import gachagacha.domain.outbox.Outbox;
import gachagacha.domain.outbox.OutboxRepository;
import gachagacha.domain.trade.Trade;
import gachagacha.domain.trade.TradeRepository;
import gachagacha.domain.trade.TradeStatus;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import gachagacha.gachaapi.notification.NotificationProcessor;
import gachagacha.storageredis.TradeRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final DecorationRepository decorationRepository;
    private final TradeRepository tradeRepository;
    private final TradeRedisRepository tradeRedisRepository;
    private final TransactionTemplate transactionTemplate;
    private final NotificationProcessor notificationProcessor;
    private final PendingJpaRepository pendingJpaRepository;

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
        Long savedTradeId = tradeRepository.save(new Trade(null, user.getId(), null, userItem.getItem(), TradeStatus.ON_SALE, null));

        try {
            tradeRedisRepository.pushTradeId(userItem.getItem().getItemId(), savedTradeId);
        } catch (RuntimeException e) {
            log.info("Redis에 거래 등록 실패");
            PendingItemStockEntity pendingItemStockEntity = new PendingItemStockEntity(null, userItem.getItem().getItemId(), savedTradeId);
            pendingJpaRepository.save(pendingItemStockEntity);
        }
    }

    private void validateUserItemAuthorization(User user, UserItem userItem) {
        if (!userItem.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    public void cancelTrade(User user, Trade trade) {
        validateTradeAuthorization(user, trade);

        user.increaseScoreByItem(trade.getItem(), userItemRepository.findByUser(user));
        userItemRepository.save(new UserItem(null, trade.getItem(), user.getId()));
        userRepository.update(user);
        tradeRepository.delete(trade);
    }

    private void validateTradeAuthorization(User user, Trade trade) {
        if (trade.getSellerId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    public Notification purchase(User buyer, Item item) {
        try {
            while (true) {
                Long tradeId = tradeRedisRepository.getTradeId(item.getItemId());
                if (tradeId == null) { // 재고 없는 경우 -> 예외 응답 반환
                    throw new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT);
                }
                Trade trade = tradeRepository.findById(tradeId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
                if (trade.getTradeStatus() == TradeStatus.ON_SALE) {
                    try {
                        return completeTrade(trade, buyer, item);
                    } catch (RuntimeException e) { // 롤백시 -> 레디스에 trade id 다시 push 후 예외 응답 반환
                        tradeRedisRepository.pushTradeId(item.getItemId(), trade.getId());
                        throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
                    }
                }
            }
        } catch (RuntimeException e) {
            if (e instanceof BusinessException) {
                throw e;
            }
            // 레디스 장애시 -> RDB에서 거래
            Trade trade = tradeRepository.findFirstOnSaleProductWithLock(item);
            return completeTrade(trade, buyer, item);
        }
    }

    private Notification completeTrade(Trade trade, User buyer, Item item) {
        return transactionTemplate.execute(status -> {
            User seller = userRepository.findById(trade.getSellerId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

            buyer.deductCoin(item.getItemGrade().getPrice());
            seller.addCoin(item.getItemGrade().getPrice());

            buyer.increaseScoreByItem(item, userItemRepository.findByUser(buyer));
            trade.processTrade(buyer);

            userItemRepository.save(new UserItem(null, item, buyer.getId()));
            userRepository.update(buyer);
            userRepository.update(seller);
            tradeRepository.update(trade);

            saveLottoIssuanceEvent(buyer.getId(), item);
            return createNotification(seller.getId(), item);
        });
    }

    private void saveLottoIssuanceEvent(Long buyerId, Item item) {
        LottoIssuanceEvent lottoIssuanceEvent = new LottoIssuanceEvent(buyerId, item.getItemGrade());
        String payload = null;
        try {
            payload = objectMapper.writeValueAsString(lottoIssuanceEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        outboxRepository.save(new Outbox(null, topic, payload));
    }

    private Notification createNotification(Long sellerId, Item item) {
        Long notificationId = notificationProcessor.saveTradeCompletedNotification(sellerId, item);
        return notificationProcessor.findById(notificationId);
    }

    public Trade readById(long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
    }
}
