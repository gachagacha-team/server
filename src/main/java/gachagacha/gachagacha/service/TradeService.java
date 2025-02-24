package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.*;
import gachagacha.gachagacha.implementation.trade.TradeAppender;
import gachagacha.gachagacha.implementation.trade.TradeReader;
import gachagacha.gachagacha.implementation.trade.TradeRemover;
import gachagacha.gachagacha.implementation.trade.TradeUpdater;
import gachagacha.gachagacha.implementation.user.UserReader;
import gachagacha.gachagacha.implementation.user.UserUpdator;
import gachagacha.gachagacha.implementation.userItem.UserItemAppender;
import gachagacha.gachagacha.implementation.userItem.UserItemReader;
import gachagacha.gachagacha.implementation.userItem.UserItemRemover;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

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
    private final UserUpdator userUpdator;
    private final TradeUpdater tradeUpdater;

    public List<Trade> readOnSaleProductsByItem(Item item) {
        return tradeReader.findOnSaleProductsByItem(item);
    }

    public Slice<Trade> readMyProducts(Pageable pageable, User user) {
        return tradeReader.findBySeller(user, pageable);
    }

    public void registerTrade(UserItem userItem, User user) {
        userItemRemover.delete(userItem);

        user.decreaseScoreForSaleItem(userItem.getItem(), userItemReader.findAllByUser(user));

        userUpdator.update(user);
        tradeAppender.save(Trade.of(user, userItem.getItem()));
    }

    public void cancelTrade(Trade trade, User user) {
        user.increaseScoreByItem(trade.getItem(), userItemReader.findAllByUser(user));
        userItemAppender.addUserItem(UserItem.of(user, trade.getItem()));
        userUpdator.update(user);
        tradeRemover.delete(trade);
    }

    public void purchase(Item item, User buyer) {
        Trade trade = tradeReader.findOnSaleProductByItem(item);
        User seller = userReader.findById(trade.getSellerId());

        buyer.deductCoin(item.getItemGrade().getPrice());
        seller.addCoin(item.getItemGrade().getPrice());

        buyer.increaseScoreByItem(item, userItemReader.findAllByUser(buyer));
        trade.processTrade(buyer);

        userItemAppender.addUserItem(UserItem.of(buyer, item));
        userUpdator.update(buyer);
        userUpdator.update(seller);
        tradeUpdater.update(trade);
    }

    public Trade readById(long tradeId) {
        return tradeReader.findById(tradeId);
    }
}
