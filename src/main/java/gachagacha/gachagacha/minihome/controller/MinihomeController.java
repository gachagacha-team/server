package gachagacha.gachagacha.minihome.controller;

import gachagacha.gachagacha.minihome.dto.*;
import gachagacha.gachagacha.minihome.service.MinihomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MinihomeController {

    private final MinihomeService minihomeService;

    @Operation(summary = "미니홈 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/minihomes/{nickname}")
    public MinihomeResponse readMinihome(@PathVariable String nickname, HttpServletRequest request) {
        return minihomeService.readMinihome(nickname, request);
    }

    @Operation(summary = "방명록 조회(페이지네이션)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @Parameter(name = "pageable", description = "최신순(sort=createdAt,desc)")
    @GetMapping("/guestbooks/{nickname}")
    public Page<GuestbookResponse> readGuestbooks(@PathVariable String nickname, Pageable pageable, HttpServletRequest request) {
        return minihomeService.readGuestbooks(nickname, pageable, request);
    }

    @Operation(summary = "방명록 등록")
    @PostMapping("/guestbooks/{nickname}")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    public GuestbookResponse addGuestbook(@PathVariable String nickname, @RequestBody AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        return minihomeService.addGuestbook(nickname, addGuestbookRequest, request);
    }

    @Operation(summary = "방명록 수정")
    @Parameter(name = "guestbookId", description = "수정할 방명록 id")
    @PutMapping("/guestbooks/{guestbookId}")
    public GuestbookResponse editGuestBook(@PathVariable long guestbookId, @RequestBody EditGuestbookRequest editGuestbookRequest,
                                           HttpServletRequest request) {
        return minihomeService.editGuestbook(guestbookId, editGuestbookRequest, request);
    }

    @Operation(summary = "방명록 삭제")
    @Parameter(name = "guestbookId", description = "삭제할 방명록 id")
    @DeleteMapping("/guestbooks/{guestbookId}")
    public void deleteGuestBook(@PathVariable long guestbookId, HttpServletRequest request) {
        minihomeService.deleteGuestbook(guestbookId, request);
    }

    @Operation(summary = "미니홈 리스트 조회(가입순, 인기순)(무한스크롤)")
    @Parameter(name = "pageable", description = "가입순(sort=createdAt,desc), 인기순(sort=totalVisitorCnt,desc)")
    @GetMapping("/explore/minihome")
    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeService.explore(pageable);
    }

    @Operation(summary = "미니홈 리스트 조회(스코어순)(무한스크롤)")
    @Parameter(name = "pageable", description = "스코어순(sort=score,desc)")
    @GetMapping("/explore/minihome/score")
    public Slice<ExploreMinihomeResponse> exploreByScore(Pageable pageable) {
        return minihomeService.exploreByScore(pageable);
    }
}
