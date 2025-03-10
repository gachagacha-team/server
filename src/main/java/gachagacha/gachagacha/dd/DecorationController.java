package gachagacha.gachagacha.dd;

import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.dd.dto.Decoration;
import gachagacha.gachagacha.dd.dto.ReadDecorationResponse;
import gachagacha.gachagacha.domain.user.Background;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DecorationController {

    private final UserService userService;
    private final DecorationService decorationService;

    @Value("${image.api.endpoints.backgrounds}")
    private String backgroundsImageApiEndpoint;

    @Operation(summary = "꾸미기 공간 저장")
    @PutMapping("/decoration/{nickname}")
    public ApiResponse updateDecoration(@PathVariable String nickname,
                                        @RequestBody UpdateDecorationRequest requestDto) {
        List<Decoration.DecorationItem> decorationItems = requestDto.getItems().stream()
                .map(decorationItemRequest -> Decoration.DecorationItem.of(decorationItemRequest.getItemId(), decorationItemRequest.getUserItemId(), decorationItemRequest.getX(), decorationItemRequest.getY()))
                .toList();
        Decoration decoration = Decoration.of(requestDto.getBackgroundId(), decorationItems);
        User user = userService.readUserByNickname(nickname);
        decorationService.save(decoration, user);
        return ApiResponse.success();
    }

    @Operation(summary = "꾸미기 공간 조회")
    @GetMapping("/decoration/{nickname}")
    public ApiResponse readDecoration(@PathVariable String nickname) {
        User user = userService.readUserByNickname(nickname);
        Decoration decoration = decorationService.read(user);
        Background background = Background.findById(decoration.getBackgroundId());
        ReadDecorationResponse readDecorationResponse = ReadDecorationResponse.of(backgroundsImageApiEndpoint, decoration, background);
        return ApiResponse.success(readDecorationResponse);
    }
}
