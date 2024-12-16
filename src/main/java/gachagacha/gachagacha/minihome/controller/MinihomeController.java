package gachagacha.gachagacha.minihome.controller;

import gachagacha.gachagacha.minihome.dto.AddGuestbookResponse;
import gachagacha.gachagacha.minihome.service.MinihomeService;
import gachagacha.gachagacha.minihome.dto.AddGuestbookRequest;
import gachagacha.gachagacha.minihome.dto.GuestbookResponse;
import gachagacha.gachagacha.minihome.dto.MinihomeResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MinihomeController {

    /*
    <minihome>
    /minihome/{nickname}
    /출석체크

   <꾸미는 창>
   내가 가진 배경, 아이템 반환
   저장

   <둘러보기>


     */

    private final MinihomeService minihomeService;

    @GetMapping("/minihome/{nickname}")
    public MinihomeResponse getMinihome(@PathVariable String nickname) {
        return minihomeService.getMinihome(nickname);
    }

    @GetMapping("/minihome/{nickname}/guestbook")
    public Slice<GuestbookResponse> getGuestbook(@PathVariable String nickname, Pageable pageable) {
        return minihomeService.getGuestbook(nickname, pageable);
    }

    @PostMapping("/minihome/{nickname}/guestbook")
    public AddGuestbookResponse addGuestbook(@PathVariable String nickname, @RequestBody AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        return minihomeService.addGuestbook(nickname, addGuestbookRequest, request);
    }
}
