package gachagacha.gachagacha.trade.controller;

import gachagacha.gachagacha.trade.dto.*;
import gachagacha.gachagacha.trade.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "상품 등록 시 등록할 내가 가진 아이템 리스트 조회(페이지네이션)")
    @GetMapping("/items/me/forSale")
    public Page<ReadItemForSaleResponse> readMyItemsForSale(HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable) {
        return tradeService.readMyItemsForSale(request, pageable);
    }

    @Operation(summary = "상품 등록")
    @PostMapping("/products")
    public void addProduct(@RequestBody AddProductRequest addProductRequest, HttpServletRequest request) {
        tradeService.registerTrade(addProductRequest, request);
    }

    @Operation(summary = "상품 조회")
    @Parameter(name = "grade", description = "조회할 상품의 아이템 등급(S, A, B, C, D)(생략 시 모든 상품 조회)")
    @GetMapping("/products")
    public List<ReadAllProductsResponse> readAllProducts(@RequestParam(value = "grade", required = false) String grade) {
        return tradeService.readProducts(grade);
    }

    @Operation(summary = "내 상품 조회")
    @Parameter(name = "grade", description = "조회할 상품의 아이템 등급(S, A, B, C, D)(생략 시 모든 상품 조회)")
    @Parameter(name = "pageable", description = "최신순(sort=createdAt,desc), 오래된순(sort=createdAt,asc)")
    @GetMapping("/products/me")
    public Slice<ReadMyOneProductResponse> readMyProducts(@RequestParam(value = "grade", required = false) String grade, Pageable pageable, HttpServletRequest request) {
        return tradeService.readMyProducts(grade, pageable, request);
    }

    @Operation(summary = "단일 상품 정보 조회")
    @Parameter(name = "itemId", description = "조회할 상품 아이템 id")
    @GetMapping("/items/{itemId}/products")
    public ReadOneProductResponse readOneProduct(@PathVariable long itemId) {
        return tradeService.readOneProduct(itemId);
    }

    @Operation(summary = "등록된 상품 취소")
    @Parameter(name = "productId", description = "취소할 상품 id")
    @DeleteMapping("/products/{productId}")
    public void cancelTrade(@PathVariable long productId, HttpServletRequest request) {
        tradeService.cancelTrade(productId, request);
    }

    @Operation(summary = "상품 구매")
    @Parameter(name = "itemId", description = "구매할 상품 아이템 id")
    @PostMapping("/items/{itemId}/purchase")
    public void purchase(@PathVariable long itemId, HttpServletRequest request) {
        tradeService.purchase(itemId, request);
    }
}
