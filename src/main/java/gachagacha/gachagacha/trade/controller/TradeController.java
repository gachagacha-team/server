package gachagacha.gachagacha.trade.controller;

import gachagacha.gachagacha.trade.dto.*;
import gachagacha.gachagacha.trade.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/marketplace/item")
    public AddTradeResponse addTrade(@RequestBody AddTradeRequest addTradeRequest, HttpServletRequest request) {
        return tradeService.addTrade(addTradeRequest, request);
    }

    @PostMapping("/marketplace/cancel/{tradeId}")
    public void deleteTrade(@PathVariable long tradeId, HttpServletRequest request) {
        tradeService.cancelTrade(tradeId, request);
    }

    @PostMapping("/marketplace/buy/{tradeId}")
    public PurchaseResponse purchase(@PathVariable long tradeId, HttpServletRequest request) {
        return tradeService.purchase(tradeId, request);
    }

    @GetMapping("/marketplace/on_sale")
    public Slice<ReadOnSaleTradeResponse> readOnSaleTrades(Pageable pageable) {
        return tradeService.readOnSaleTrades(pageable);
    }

    @GetMapping("/marketplace/my_products")
    public Slice<ReadMyProductTradeResponse> readOnSaleTrades(Pageable pageable, HttpServletRequest request) {
        return tradeService.readMyProductTrades(pageable, request);
    }
}
