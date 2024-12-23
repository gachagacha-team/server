package gachagacha.gachagacha.trade.controller;

import gachagacha.gachagacha.trade.dto.*;
import gachagacha.gachagacha.trade.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/market/on_sale")
    public Slice<ReadTradesResponse> readOnSaleTrades(Pageable pageable) {
        return tradeService.readOnSaleTrades(pageable);
    }

    @GetMapping("/market/sold")
    public Slice<ReadTradesResponse> readSoldTrades(Pageable pageable) {
        return tradeService.readSoldTrades(pageable);
    }

    @GetMapping("/market/my_products")
    public Slice<ReadTradesResponse> readMyProductTrades(Pageable pageable, HttpServletRequest request) {
        return tradeService.readMyProductTrades(pageable, request);
    }

    @GetMapping("/market/trade/{tradeId}")
    public ReadTradeResponse readTrade(@PathVariable long tradeId) {
        return tradeService.readTrade(tradeId);
    }

    @PutMapping("/market/trade/{tradeId}")
    public ReadTradeResponse editTrade(@PathVariable long tradeId, EditTradeRequest editTradeRequest) {
        return tradeService.editTrade(tradeId, editTradeRequest);
    }

    @GetMapping("/market/sell/{itemId}")
    public ReadItemTradeResponse getItemForSell(@PathVariable long itemId) {
        return tradeService.readItemTrade(itemId);
    }

    @PostMapping("/market/sell")
    public AddTradeResponse addTrade(@RequestBody AddTradeRequest addTradeRequest, HttpServletRequest request) {
        return tradeService.addTrade(addTradeRequest, request);
    }

    @PostMapping("/market/buy/{tradeId}")
    public BuyItemResponse buy(@PathVariable long tradeId, HttpServletRequest request) {
        return tradeService.buy(tradeId, request);
    }
}
