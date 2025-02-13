package gachagacha.gachagacha.minihome.controller;

import gachagacha.gachagacha.minihome.dto.*;
import gachagacha.gachagacha.minihome.service.MinihomeService;
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

    @GetMapping("/minihomes/{nickname}")
    public MinihomeResponse readMinihome(@PathVariable String nickname, HttpServletRequest request) {
        return minihomeService.readMinihome(nickname, request);
    }

    @GetMapping("/guestbooks/{nickname}")
    public Page<GuestbookResponse> readGuestbooks(@PathVariable String nickname, Pageable pageable, HttpServletRequest request) {
        return minihomeService.readGuestbooks(nickname, pageable, request);
    }

    @PostMapping("/guestbooks/{nickname}")
    public GuestbookResponse addGuestbook(@PathVariable String nickname, @RequestBody AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        return minihomeService.addGuestbook(nickname, addGuestbookRequest, request);
    }

    @PutMapping("/guestbooks/{guestbookId}")
    public GuestbookResponse editGuestBook(@PathVariable long guestbookId, @RequestBody EditGuestbookRequest editGuestbookRequest) {
        return minihomeService.editGuestbook(guestbookId, editGuestbookRequest);
    }

    @DeleteMapping("/guestbooks/{guestbookId}")
    public void deleteGuestBook(@PathVariable long guestbookId) {
        minihomeService.deleteGuestbook(guestbookId);
    }

    @GetMapping("/explore/minihome")
    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeService.explore(pageable);
    }
}
