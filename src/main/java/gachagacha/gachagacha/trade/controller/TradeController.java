package gachagacha.gachagacha.trade.controller;

import gachagacha.gachagacha.trade.dto.*;
import gachagacha.gachagacha.trade.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/items/me/forSale")
    public Page<ReadItemForSaleResponse> readMyItemsForSale(HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable) {
        return tradeService.readMyItemsForSale(request, pageable);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody AddProductRequest addProductRequest, HttpServletRequest request) {
        tradeService.registerTrade(addProductRequest, request);
    }

    @GetMapping("/products")
    public List<ReadAllProductsResponse> readAllProducts(@RequestParam(value = "grade", required = false) String grade) {
        return tradeService.readProducts(grade);
    }

    @GetMapping("/products/me")
    public Slice<ReadMyOneProductResponse> readMyProducts(@RequestParam(value = "grade", required = false) String grade, Pageable pageable, HttpServletRequest request) {
        return tradeService.readMyProducts(grade, pageable, request);
    }

    @GetMapping("/items/{itemId}/products")
    public ReadOneProductResponse readOneProduct(@PathVariable long itemId) {
        return tradeService.readOneProduct(itemId);
    }

    @DeleteMapping("/products/{productId}")
    public void cancelTrade(@PathVariable long productId, HttpServletRequest request) {
        tradeService.cancelTrade(productId, request);
    }

    @PostMapping("/items/{itemId}/purchase")
    public void purchase(@PathVariable long itemId, HttpServletRequest request) {
        tradeService.purchase(itemId, request);
    }
}
