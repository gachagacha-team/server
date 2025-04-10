package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.request.UpdateDecorationRequest;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.decoration.Decoration;
import gachagacha.gachagacha.api.dto.response.ReadDecorationResponse;
import gachagacha.gachagacha.domain.decoration.DecorationService;
import gachagacha.gachagacha.domain.item.ItemService;
import gachagacha.gachagacha.domain.item.UserItem;
import gachagacha.gachagacha.domain.user.Background;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DecorationController {

    private final UserService userService;
    private final DecorationService decorationService;
    private final ItemService itemService;
    private final JwtUtils jwtUtils;

    @Value("${image.api.endpoints.backgrounds}")
    private String backgroundsImageApiEndpoint;

    @Value("${image.api.endpoints.items}")
    private String itemsImageApiEndpoint;

    @Operation(summary = "꾸미기 공간 저장")
    @PutMapping("/decoration/{nickname}")
    public ApiResponse updateDecoration(@PathVariable String nickname, @RequestBody UpdateDecorationRequest requestDto, HttpServletRequest request) {

        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        if (!user.getNickname().equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Background background = Background.findById(requestDto.getBackgroundId());

        List<Decoration.DecorationItem> decorationItems = requestDto.getItems().stream()
                .map(decorationItemRequest -> {
                    UserItem userItem = itemService.readById(decorationItemRequest.getSubId());
                    if (userItem.getItem().getItemId() != decorationItemRequest.getItemId()) {
                        throw new BusinessException(ErrorCode.INVALID_ITEM_ID);
                    }
                    userItem.isOwnedBy(user);
                    return Decoration.DecorationItem.of(decorationItemRequest.getItemId(), decorationItemRequest.getSubId(), decorationItemRequest.getX(), decorationItemRequest.getY());
                })
                .toList();

        Decoration decoration = Decoration.of(background, decorationItems);
        decorationService.save(decoration, user);
        return ApiResponse.success();
    }

    @Operation(summary = "꾸미기 공간 조회")
    @GetMapping("/decoration/{nickname}")
    public ApiResponse<ReadDecorationResponse> readDecoration(@PathVariable String nickname) {
        User user = userService.readUserByNickname(nickname);
        Decoration decoration = decorationService.read(user);
        Background background = Background.findById(decoration.getBackgroundId());
        ReadDecorationResponse readDecorationResponse = ReadDecorationResponse.of(decoration, background, itemsImageApiEndpoint, backgroundsImageApiEndpoint);
        return ApiResponse.success(readDecorationResponse);
    }
}
