package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.response.GachaResponse;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.ItemService;
import gachagacha.gachagacha.domain.lotto.LottoProcessor;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GachaController {

    private final JwtUtils jwtUtils;
    private final ItemService itemService;
    private final UserService userService;
    private final LottoProcessor lottoProcessor;

    @Value("${image.api.endpoints.items}")
    private String itemsImageApiEndpoint;

    @Operation(summary = "아이템 뽑기")
    @PostMapping("/gacha")
    public ApiResponse<GachaResponse> gacha(HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        Item addedItem = itemService.gacha(user);
        lottoProcessor.checkAndPublishLotteryEvent(user, addedItem);
        return ApiResponse.success(GachaResponse.of(addedItem, itemsImageApiEndpoint));
    }
}
