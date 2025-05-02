package gachagacha.gachaapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gachagacha.domain.notification.Notification;
import gachagacha.gachaapi.auth.jwt.JwtUtils;
import gachagacha.gachaapi.service.SseService;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.ItemGrade;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.trade.Trade;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import gachagacha.gachaapi.dto.response.UserItemsForSaleResponse;
import gachagacha.gachaapi.dto.request.AddProductRequest;
import gachagacha.gachaapi.dto.response.ReadAllProductsResponse;
import gachagacha.gachaapi.dto.response.ReadMyOneProductResponse;
import gachagacha.gachaapi.dto.response.ReadOneProductResponse;
import gachagacha.gachaapi.service.ItemService;
import gachagacha.gachaapi.service.TradeService;
import gachagacha.gachaapi.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MarketController {

    private final TradeService tradeService;
    private final UserService userService;
    private final ItemService itemService;
    private final JwtUtils jwtUtils;
    private final SseService sseService;

    @Value("${image.api.endpoints.items}")
    private String itemsImageApiEndpoint;

    @Operation(summary = "전체 상품 조회")
    @Parameter(name = "grade", description = "조회할 상품의 아이템 등급(S, A, B, C, D)(생략 시 모든 상품 조회)")
    @GetMapping("/products")
    public ApiResponse<List<ReadAllProductsResponse>> readAllProducts(@RequestParam(value = "grade", required = false) String grade) {
        List<Item> items = (grade == null) ? Arrays.asList(Item.values()) : Item.getItemsByGrade(ItemGrade.findByViewName(grade));

        return ApiResponse.success(items.stream()
                .map(item -> {
                    int stock = tradeService.readOnSaleProductsByItem(item).size();
                    return ReadAllProductsResponse.of(item, stock, itemsImageApiEndpoint);
                })
                .sorted(Comparator.comparing(ReadAllProductsResponse::isHasStock).reversed())
                .toList());
    }

    @Operation(summary = "단일 상품 정보 조회")
    @Parameter(name = "itemId", description = "조회할 상품 아이템 id")
    @GetMapping("/items/{itemId}/products")
    public ApiResponse<ReadOneProductResponse> readOneProduct(@PathVariable long itemId) {
        Item item = Item.findById(itemId);
        int stock = tradeService.readOnSaleProductsByItem(item).size();
        return ApiResponse.success(ReadOneProductResponse.of(item, stock, itemsImageApiEndpoint));
    }

    @Operation(summary = "상품 구매")
    @Parameter(name = "itemId", description = "구매할 상품 아이템 id")
    @PostMapping("/items/{itemId}/purchase")
    public ApiResponse purchase(@PathVariable long itemId, HttpServletRequest request) throws JsonProcessingException {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        Item purchaseItem = Item.findById(itemId);
        Notification notification = tradeService.purchase(user, purchaseItem);
        sseService.tradeComplete(notification); // todo: sse 알림 책임은 어느 모듈에서?
        return ApiResponse.success();
    }

    @Operation(summary = "내 판매 목록 조회")
    @Parameter(name = "grade", description = "조회할 상품의 아이템 등급(S, A, B, C, D)(생략 시 모든 상품 조회)")
    @Parameter(name = "pageable", description = "최신순(sort=createdAt,desc), 오래된순(sort=createdAt,asc)")
    @GetMapping("/products/me")
    public ApiResponse<Slice<ReadMyOneProductResponse>> readMyProducts(@RequestParam(value = "grade", required = false) String grade, Pageable pageable, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        Slice<Trade> tradesSlice = tradeService.readMyProducts(pageable, user);
        List<Trade> trades = tradesSlice.getContent();
        if (grade != null) {
            ItemGrade itemGrade = ItemGrade.findByViewName(grade);
            trades = trades.stream()
                    .filter(trade -> trade.getItem().getItemGrade() == itemGrade)
                    .toList();
        }
        List<ReadMyOneProductResponse> ReadMyOneProductResponses = trades.stream()
                .map(trade -> ReadMyOneProductResponse.of(trade, itemsImageApiEndpoint))
                .toList();

        return ApiResponse.success(
                new SliceImpl<>(ReadMyOneProductResponses, pageable, tradesSlice.hasNext())
        );
    }

    @Operation(summary = "판매 취소")
    @Parameter(name = "productId", description = "취소할 상품 id")
    @DeleteMapping("/products/{productId}")
    public ApiResponse cancelTrade(@PathVariable long productId, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        Trade trade = tradeService.readById(productId);
        tradeService.cancelTrade(user, trade);
        return ApiResponse.success();
    }

    @Operation(summary = "상품 등록 시 내가 가진 아이템 조회(페이지네이션)")
    @GetMapping("/items/me")
    public ApiResponse<Page<UserItemsForSaleResponse>> readMyItemsForSale(HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));

        // 미니홈에 있는 아이템 제외하고 user item 리스트 조회
        List<UserItem> userItems = itemService.readUserItemsExcludeDecorationItem(user);

        // user item 리스트를 item 타입별로 그룹화
        Map<Item, List<UserItem>> groupedByItem = userItems.stream()
                .collect(Collectors.groupingBy(UserItem::getItem));

        // 각 item마다 응답 생성
        List<UserItemsForSaleResponse> responseDto = groupedByItem.keySet().stream()
                .map(item -> {
                    int stock = tradeService.readOnSaleProductsByItem(item).size();
                    return UserItemsForSaleResponse.of(item, groupedByItem.get(item), stock, itemsImageApiEndpoint);
                })
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), responseDto.size());
        List<UserItemsForSaleResponse> pagedList = responseDto.subList(start, end);
        Page<UserItemsForSaleResponse> page = new PageImpl<>(pagedList, pageable, responseDto.size());
        return ApiResponse.success(page);
    }

    @Operation(summary = "상품 등록")
    @PostMapping("/products")
    public ApiResponse addProduct(@RequestBody AddProductRequest addProductRequest, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        UserItem userItem = itemService.readById(addProductRequest.getUserItemId());
        tradeService.registerTrade(user, userItem);
        return ApiResponse.success();
    }
}
