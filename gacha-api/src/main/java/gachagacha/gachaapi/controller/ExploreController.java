package gachagacha.gachaapi.controller;

import gachagacha.domain.minihome.Minihome;
import gachagacha.gachaapi.dto.response.ExploreMinihomeResponse;
import gachagacha.gachaapi.common.ApiResponse;
import gachagacha.gachaapi.service.MinihomeService;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExploreController {

    private final MinihomeService minihomeService;
    private final UserService userService;

    @Operation(summary = "미니홈 리스트 조회(가입순, 인기순)(무한스크롤)")
    @Parameter(name = "pageable", description = "가입순(sort=createdAt,desc), 인기순(sort=totalVisitorCnt,desc)")
    @GetMapping("/explore/minihome")
    public ApiResponse<Slice<ExploreMinihomeResponse>> explore(Pageable pageable) {
        Slice<ExploreMinihomeResponse> data = minihomeService.explore(pageable)
                .map(minihome -> {
                    User user = userService.readUserById(minihome.getUserId());
                    return ExploreMinihomeResponse.of(minihome, user);
                });
        return ApiResponse.success(data);
    }

    @Operation(summary = "미니홈 리스트 조회(스코어순)(무한스크롤)")
    @Parameter(name = "pageable", description = "스코어순(sort=score,desc)")
    @GetMapping("/explore/minihome/score")
    public ApiResponse<Slice<ExploreMinihomeResponse>> exploreByScore(Pageable pageable) {
        Slice<ExploreMinihomeResponse> data = minihomeService.exploreByScore(pageable)
                .map(minihome -> {
                    User user = userService.readUserById(minihome.getUserId());
                    return ExploreMinihomeResponse.of(minihome, user);
                });
        return ApiResponse.success(data);
    }

    @Operation(summary = "미니홈 리스트 조회(좋아요순)(무한스크롤)")
    @Parameter(name = "pageable", description = "좋아요순(sort=likeCount,desc)")
    @GetMapping("/explore/minihome/like_count")
    public ApiResponse<Slice<ExploreMinihomeResponse>> exploreByLikeCount(Pageable pageable) {
        Slice<ExploreMinihomeResponse> data = minihomeService.exploreByLikeCount(pageable)
                .map(minihome -> {
                    User user = userService.readUserById(minihome.getUserId());
                    return ExploreMinihomeResponse.of(minihome, user);
                });
        return ApiResponse.success(data);
    }
}
