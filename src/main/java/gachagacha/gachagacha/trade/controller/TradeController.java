package gachagacha.gachagacha.trade.controller;

import gachagacha.gachagacha.trade.dto.AddTradeRequest;
import gachagacha.gachagacha.trade.dto.AddTradeResponse;
import gachagacha.gachagacha.trade.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/trade/item")
    public AddTradeResponse addTrade(@RequestBody AddTradeRequest addTradeRequest, HttpServletRequest request) {
        return tradeService.addTrade(addTradeRequest, request);
    }
}
