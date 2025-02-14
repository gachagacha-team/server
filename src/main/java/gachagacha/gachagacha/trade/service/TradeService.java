package gachagacha.gachagacha.trade.service;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.ItemGrade;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.trade.dto.*;
import gachagacha.gachagacha.trade.entity.Trade;
import gachagacha.gachagacha.trade.entity.TradeStatus;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.trade.repository.TradeRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final TradeRepository tradeRepository;

    @Transactional(readOnly = true)
    public List<ReadAllProductsResponse> readProducts(String grade) {
        List<Item> items = (grade == null) ? Arrays.asList(Item.values()) : Item.getItemsByGrade(ItemGrade.findByViewName(grade));
        return items.stream()
                .map(item -> {
                    List<Trade> trades = tradeRepository.findByItem(item);
                    return new ReadAllProductsResponse(item.getItemId(), "/image/items/" + item.getImageFileName(), !trades.isEmpty());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ReadOneProductResponse readOneProduct(long itemId) {
        Item item = Item.findById(itemId);
        List<Trade> trades = tradeRepository.findByItem(item);
        return new ReadOneProductResponse(item.getViewName(), item.getItemGrade().getViewName(), trades.size(), "/image/items/" + item.getImageFileName());
    }

    @Transactional(readOnly = true)
    public Slice<ReadMyOneProductResponse> readMyProducts(String grade, Pageable pageable, HttpServletRequest request) {
        User seller = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Slice<Trade> productSlice = tradeRepository.findBySeller(seller, pageable);
        List<Trade> trades = productSlice.getContent();

        if (grade != null) {
            trades = filterByGrade(trades, ItemGrade.findByViewName(grade));
        }

        List<ReadMyOneProductResponse> ReadMyOneProductResponses = trades.stream()
                .map(trade -> {
                    ReadMyOneProductResponse readMyOneProductResponse = new ReadMyOneProductResponse(trade.getId(),
                            "/image/items/" + trade.getItem().getImageFileName(),
                            trade.getItem().getViewName(),
                            trade.getItem().getItemGrade().getViewName(),
                            trade.getItem().getItemGrade().getPrice(),
                            trade.getTradeStatus().getViewName()
                    );
                    if (trade.getTradeStatus() == TradeStatus.COMPLETED) {
                        readMyOneProductResponse.setTransactionDate(trade.getTransactionDate());
                    }
                    return readMyOneProductResponse;
                })
                .toList();

        return new SliceImpl<>(ReadMyOneProductResponses, pageable, productSlice.hasNext());
    }

    private List<Trade> filterByGrade(List<Trade> trades, ItemGrade itemGrade) {
        return trades.stream()
                .filter(trade -> trade.getItem().getItemGrade() == itemGrade)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ReadItemForSaleResponse> readMyItemsForSale(HttpServletRequest request, Pageable pageable) {
        User user = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        return userItemRepository.findByUserNicknameSlice(user.getNickname(), pageable)
                .map(userItem -> new ReadItemForSaleResponse(userItem.getId(), userItem.getItem().getViewName(),
                        userItem.getItem().getItemGrade().getViewName(), userItem.getItem().getItemGrade().getPrice(),
                        "/image/items/" + userItem.getItem().getImageFileName()));
    }

    @Transactional
    public void registerTrade(AddProductRequest addProductRequest, HttpServletRequest request) {
        UserItem userItem = userItemRepository.findById(addProductRequest.getUserItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        User seller = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        seller.saleUserItem(userItem);
        tradeRepository.save(Trade.create(seller, userItem.getItem()));
        userItemRepository.delete(userItem);
    }

    @Transactional
    public void cancelTrade(long productId, HttpServletRequest request) {
        User seller = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Trade trade = tradeRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        seller.cancelTrade(trade);
        tradeRepository.delete(trade);
    }

    @Transactional
    public void purchase(long itemId, HttpServletRequest request) {
        User buyer = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Item item = Item.findById(itemId);

        Trade trade = tradeRepository.findByItemOrderByCreatedAtAsc(item)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT));

        buyer.processPurchase(trade.getItem());
        trade.getSeller().completeSale(trade.getItem());
        trade.processTrade(buyer);
    }
}
