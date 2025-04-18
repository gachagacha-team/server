package gachagacha.gachagacha.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import gachagacha.gachagacha.api.dto.response.GachaResponse;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.ItemService;
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

    @Value("${image.api.endpoints.items}")
    private String itemsImageApiEndpoint;

    @Operation(summary = "아이템 뽑기")
    @PostMapping("/gacha")
    public ApiResponse<GachaResponse> gacha(HttpServletRequest request) throws JsonProcessingException {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        Item addedItem = itemService.gacha(user);
        return ApiResponse.success(GachaResponse.of(addedItem, itemsImageApiEndpoint));
    }
}
