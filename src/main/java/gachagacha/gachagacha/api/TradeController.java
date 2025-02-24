package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.*;
import gachagacha.gachagacha.domain.*;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.service.ItemService;
import gachagacha.gachagacha.service.TradeService;
import gachagacha.gachagacha.service.UserService;
import gachagacha.gachagacha.support.api_response.ApiResponse;
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
public class TradeController {

    private final TradeService tradeService;
    private final UserService userService;
    private final ItemService itemService;
    private final JwtUtils jwtUtils;
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
    public ApiResponse purchase(@PathVariable long itemId, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        tradeService.purchase(Item.findById(itemId), user);
        return ApiResponse.success();
    }

    @Operation(summary = "내 상품 조회")
    @Parameter(name = "grade", description = "조회할 상품의 아이템 등급(S, A, B, C, D)(생략 시 모든 상품 조회)")
    @Parameter(name = "pageable", description = "최신순(sort=createdAt,desc), 오래된순(sort=createdAt,asc)")
    @GetMapping("/products/me")
    public ApiResponse<Slice<ReadMyOneProductResponse>> readMyProducts(@RequestParam(value = "grade", required = false) String grade, Pageable pageable, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
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

    @Operation(summary = "상품 등록 시 등록할 내가 가진 아이템 리스트 조회(페이지네이션)")
    @GetMapping("/items/me/forSale")
    public ApiResponse<Page<UserItemForSaleResponse>> readMyItemsForSale(HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));

        Map<Item, Integer> userItemCountMap = Arrays.stream(Item.values())
                .map(item -> {
                    int userItemCnt = itemService.readUserItemsByItem(user, item).size();
                    return userItemCnt > 0 ? Map.entry(item, userItemCnt) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<UserItemForSaleResponse> data = userItemCountMap.keySet().stream()
                .map(item -> {
                    int stock = tradeService.readOnSaleProductsByItem(item).size();
                    return UserItemForSaleResponse.of(item, userItemCountMap.get(item), stock, itemsImageApiEndpoint);
                })
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), data.size());
        List<UserItemForSaleResponse> pagedList = data.subList(start, end);
        Page<UserItemForSaleResponse> page = new PageImpl<>(pagedList, pageable, data.size());
        return ApiResponse.success(page);
    }

    @Operation(summary = "상품 등록")
    @PostMapping("/products")
    public ApiResponse addProduct(@RequestBody AddProductRequest addProductRequest, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        UserItem userItem = itemService.readById(addProductRequest.getUserItemId());
        if (userItem.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        tradeService.registerTrade(userItem, user);
        return ApiResponse.success();
    }

    @Operation(summary = "등록된 상품 취소")
    @Parameter(name = "productId", description = "취소할 상품 id")
    @DeleteMapping("/products/{productId}")
    public ApiResponse cancelTrade(@PathVariable long productId, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        Trade trade = tradeService.readById(productId);
        if (trade.getSellerId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        tradeService.cancelTrade(trade, user);
        return ApiResponse.success();
    }
}
