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
    public MinihomeResponse visitMinihome(@PathVariable String nickname) {
        return minihomeService.visitMinihome(nickname);
    }

    @GetMapping("/minihome/{nickname}/guestbook")
    public Slice<GuestbookResponse> getGuestbook(@PathVariable String nickname, Pageable pageable) {
        return minihomeService.getGuestbook(nickname, pageable);
    }

    @PostMapping("/minihome/{nickname}/guestbook")
    public AddGuestbookResponse addGuestbook(@PathVariable String nickname, @RequestBody AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        return minihomeService.addGuestbook(nickname, addGuestbookRequest, request);
    }

    @GetMapping("/explore")
    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeService.explore(pageable);
    }
}
