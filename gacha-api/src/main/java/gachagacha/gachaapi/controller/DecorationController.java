package gachagacha.gachaapi.controller;

import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.decoration.DecorationItem;
import gachagacha.gachaapi.dto.response.ReadDecorationResponse;
import gachagacha.gachaapi.response.ApiResponse;
import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.gachaapi.jwt.JwtUtils;
import gachagacha.gachaapi.service.DecorationService;
import gachagacha.gachaapi.service.ItemService;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.user.Background;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import gachagacha.gachaapi.dto.request.UpdateDecorationRequest;
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

        List<DecorationItem> decorationItems = requestDto.getItems().stream()
                .map(decorationItemRequest -> {
                    UserItem userItem = itemService.readById(decorationItemRequest.getSubId());
                    if (userItem.getItem().getItemId() != decorationItemRequest.getItemId()) {
                        throw new BusinessException(ErrorCode.INVALID_ITEM_ID);
                    }
                    userItem.isOwnedBy(user);
                    return new DecorationItem(userItem.getId(), userItem.getItem().getItemId(), decorationItemRequest.getX(), decorationItemRequest.getY());
                })
                .toList();

        Decoration decoration = Decoration.of(user.getId(), background, decorationItems);
        decorationService.save(decoration, user);
        return ApiResponse.success();
    }

    @Operation(summary = "꾸미기 공간 조회")
    @GetMapping("/decoration/{nickname}")
    public ApiResponse<ReadDecorationResponse> readDecoration(@PathVariable String nickname) {
        User user = userService.readUserByNickname(nickname);
        Decoration decoration = decorationService.read(user);
        ReadDecorationResponse readDecorationResponse = ReadDecorationResponse.of(decoration, itemsImageApiEndpoint, backgroundsImageApiEndpoint);
        return ApiResponse.success(readDecorationResponse);
    }
}
