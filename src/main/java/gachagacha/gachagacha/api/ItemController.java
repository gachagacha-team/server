package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.response.GachaResponse;
import gachagacha.gachagacha.api.dto.response.BackgroundResponse;
import gachagacha.gachagacha.api.dto.response.UserItemResponse;
import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.ItemGrade;
import gachagacha.gachagacha.domain.item.UserItem;
import gachagacha.gachagacha.domain.lotto.LottoProcessor;
import gachagacha.gachagacha.domain.user.Background;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.domain.item.ItemService;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final JwtUtils jwtUtils;
    private final ItemService itemService;
    private final UserService userService;
    private final LottoProcessor lottoProcessor;

    @Value("${image.api.endpoints.items}")
    private String itemsImageApiEndpoint;

    @Value("${image.api.endpoints.backgrounds}")
    private String backgroundsImageApiEndpoint;

    @Operation(summary = "아이템 뽑기")
    @PostMapping("/gacha")
    public ApiResponse<GachaResponse> gacha(HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        Item addedItem = itemService.gacha(user);
        lottoProcessor.checkAndPublishLotteryEvent(user, addedItem);
        return ApiResponse.success(GachaResponse.of(addedItem, itemsImageApiEndpoint));
    }

    @Operation(summary = "사용자가 보유한 아이템 리스트 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @Parameter(name = "grade", description = "조회할 아이템 등급(S, A, B, C, D)(생략 시 모든 아이템 조회)")
    @GetMapping("/items/{nickname}")
    public ApiResponse<List<UserItemResponse>> getItems(@PathVariable String nickname, @RequestParam(value = "grade", required = false) String grade, HttpServletRequest request) {
        String currentUserNickname = jwtUtils.getUserNicknameFromHeader(request);
        if (!currentUserNickname.equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        User user = userService.readUserByNickname(nickname);
        List<Item> items = (grade == null) ? Arrays.stream(Item.values()).toList() : Item.getItemsByGrade(ItemGrade.findByViewName(grade));
        return ApiResponse.success(items.stream()
                .map(item -> {
                            List<UserItem> userItemsByItem = itemService.readUserItemsByItem(user, item);
                            return UserItemResponse.of(item, userItemsByItem.size(), itemsImageApiEndpoint);
                        }
                ).toList());
    }

    @Operation(summary = "사용자가 보유한 배경 리스트 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/backgrounds/{nickname}")
    public ApiResponse<List<BackgroundResponse>> readAllBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        String currentUserNickname = jwtUtils.getUserNicknameFromHeader(request);
        if (!currentUserNickname.equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        User user = userService.readUserByNickname(nickname);
        List<Background> backgrounds = user.getBackgrounds();
        return ApiResponse.success(backgrounds.stream()
                .map(background -> BackgroundResponse.of(background, backgroundsImageApiEndpoint))
                .toList());
    }
}
