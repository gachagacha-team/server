package gachagacha.gachagacha.minihome.controller;

import gachagacha.gachagacha.minihome.dto.*;
import gachagacha.gachagacha.minihome.service.MinihomeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MinihomeController {

    private final MinihomeService minihomeService;

    @GetMapping("/minihome/{nickname}")
    public MinihomeResponse getMinihome(@PathVariable String nickname) {
        return minihomeService.getMinihome(nickname);
    }

    @GetMapping("/minihome/{nickname}/guestbook")
    public Slice<GuestbookResponse> getGuestbooks(@PathVariable String nickname, Pageable pageable, HttpServletRequest request) {
        return minihomeService.getGuestbooks(nickname, pageable, request);
    }

    @PostMapping("/minihome/{nickname}/guestbook")
    public GuestbookResponse addGuestbook(@PathVariable String nickname, @RequestBody AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        return minihomeService.addGuestbook(nickname, addGuestbookRequest, request);
    }

    @PutMapping("/minihome/guestbook/{guestbookId}")
    public GuestbookResponse editGuestBook(@PathVariable long guestbookId, @RequestBody EditGuestbookRequest editGuestbookRequest) {
        return minihomeService.editGuestbook(guestbookId, editGuestbookRequest);
    }

    @DeleteMapping("/minihome/guestbook/{guestbookId}")
    public void deleteGuestBook(@PathVariable long guestbookId) {
        minihomeService.deleteGuestbook(guestbookId);
    }

    @GetMapping("/explore")
    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeService.explore(pageable);
    }
}
