package gachagacha.gachaapi.controller;

import gachagacha.gachaapi.dto.response.ExploreResponse;
import gachagacha.gachaapi.common.ApiResponse;
import gachagacha.gachaapi.service.MinihomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ExploreController {

    private final MinihomeService minihomeService;

    @Operation(summary = "가입순 둘러보기(무한스크롤)")
    @Parameter(name = "createdAt", description = "이전 createdAt")
    @Parameter(name = "minihomeId", description = "이전 minihomeId")
    @Parameter(name = "size", description = "한 페이지의 사이즈")
    @GetMapping("/explore/minihome/createdAt")
    public ApiResponse<ExploreResponse> exploreByCreatedAt(@RequestParam(required = false) LocalDateTime createdAt,
                                                           @RequestParam(required = false) Long minihomeId,
                                                           Pageable pageable) {
        createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        minihomeId = minihomeId == null ? Long.MAX_VALUE : minihomeId;
        ExploreResponse data = minihomeService.exploreByCreatedAt(pageable, createdAt, minihomeId);
        return ApiResponse.success(data);
    }

    @Operation(summary = "인기순 둘러보기(무한스크롤)")
    @Parameter(name = "totalVisitorCnt", description = "이전 totalVisitorCnt")
    @Parameter(name = "minihomeId", description = "이전 minihomeId")
    @Parameter(name = "size", description = "한 페이지의 사이즈")
    @GetMapping("/explore/minihome/totalVisitorCnt")
    public ApiResponse<ExploreResponse> exploreByTotalVisitorCnt(@RequestParam(required = false) Integer totalVisitorCnt,
                                                                 @RequestParam(required = false) Long minihomeId,
                                                                 Pageable pageable) {
        totalVisitorCnt = totalVisitorCnt == null ? Integer.MAX_VALUE : totalVisitorCnt;
        minihomeId = minihomeId == null ? Long.MAX_VALUE : minihomeId;
        ExploreResponse data = minihomeService.exploreByTotalVisitorCnt(pageable, totalVisitorCnt, minihomeId);
        return ApiResponse.success(data);
    }

    @Operation(summary = "스코어순 둘러보기(무한스크롤)")
    @Parameter(name = "score", description = "이전 score")
    @Parameter(name = "userId", description = "이전 userId")
    @Parameter(name = "size", description = "한 페이지의 사이즈")
    @GetMapping("/explore/minihome/score")
    public ApiResponse<ExploreResponse> exploreByScore(@RequestParam(required = false) Integer score,
                                                       @RequestParam(required = false) Long userId,
                                                       Pageable pageable) {
        score = score == null ? Integer.MAX_VALUE : score;
        userId = userId == null ? Long.MAX_VALUE : userId;
        ExploreResponse data = minihomeService.exploreByScore(pageable, score, userId);
        return ApiResponse.success(data);
    }

    @Operation(summary = "좋아요순 둘러보기(무한스크롤)")
    @Parameter(name = "likeCount", description = "이전 likeCount")
    @Parameter(name = "minihomeId", description = "이전 minihomeId")
    @Parameter(name = "size", description = "한 페이지의 사이즈")
    @GetMapping("/explore/minihome/likeCount")
    public ApiResponse<ExploreResponse> exploreByLikeCount(@RequestParam(required = false) Long likeCount,
                                                           @RequestParam(required = false) Long minihomeId,
                                                           Pageable pageable) {
        likeCount = likeCount == null ? Long.MAX_VALUE : likeCount;
        minihomeId = minihomeId == null ? Long.MAX_VALUE : minihomeId;
        ExploreResponse data = minihomeService.exploreByLikeCount(pageable, likeCount, minihomeId);
        return ApiResponse.success(data);
    }
}
