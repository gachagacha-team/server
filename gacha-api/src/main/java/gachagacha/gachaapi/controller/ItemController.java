package gachagacha.gachaapi.controller;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.gachaapi.auth.jwt.JwtUtils;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.ItemGrade;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.user.Background;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.dto.response.BackgroundResponse;
import gachagacha.gachaapi.dto.response.UserItemResponse;
import gachagacha.gachaapi.dto.response.UserItemsResponse;
import gachagacha.gachaapi.service.ItemService;
import gachagacha.gachaapi.service.UserService;
import gachagacha.gachaapi.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final JwtUtils jwtUtils;
    private final ItemService itemService;
    private final UserService userService;

    @Value("${image.api.endpoints.items}")
    private String itemsImageApiEndpoint;

    @Value("${image.api.endpoints.backgrounds}")
    private String backgroundsImageApiEndpoint;

    @Operation(summary = "아이템북 조회 - 전체 아이템과 사용자가 보유한 아이템 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @Parameter(name = "grade", description = "조회할 아이템 등급(S, A, B, C, D)(생략 시 모든 아이템 조회)")
    @GetMapping("/itembook/{nickname}")
    public ApiResponse<List<UserItemResponse>> getItems(@PathVariable String nickname, @RequestParam(value = "grade", required = false) String grade, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        if (!user.getNickname().equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        List<Item> items = (grade == null) ? Arrays.stream(Item.values()).toList() : Item.getItemsByGrade(ItemGrade.findByViewName(grade));
        return ApiResponse.success(items.stream()
                .map(item -> {
                            List<UserItem> userItemsByItem = itemService.readUserItemsByItem(user, item);
                            return UserItemResponse.of(item, userItemsByItem.size(), itemsImageApiEndpoint);
                        }
                ).toList());
    }

    @Operation(summary = "미니홈 꾸미기 - 사용자가 보유한 아이템 조회(페이지네이션)")
    @GetMapping("/items/{nickname}")
    public ApiResponse<Page<UserItemsResponse>> readMyItems(@PathVariable String nickname, HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        if (!user.getNickname().equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        List<UserItemsResponse> data = itemService.readAllUserItems(user)
                .stream()
                .map(userItem -> UserItemsResponse.of(userItem, itemsImageApiEndpoint))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), data.size());
        List<UserItemsResponse> pagedList = data.subList(start, end);
        Page<UserItemsResponse> page = new PageImpl<>(pagedList, pageable, data.size());
        return ApiResponse.success(page);
    }

    @Operation(summary = "미니홈 꾸미기 - 사용자가 보유한 배경 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/backgrounds/{nickname}")
    public ApiResponse<List<BackgroundResponse>> readAllBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        if (!user.getNickname().equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        List<Background> backgrounds = userService.readUserBackgrounds(user);

        return ApiResponse.success(backgrounds.stream()
                .map(background -> BackgroundResponse.of(background, backgroundsImageApiEndpoint))
                .toList());
    }
}
