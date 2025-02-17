package gachagacha.gachagacha.item.controller;

import gachagacha.gachagacha.item.dto.GachaResponse;
import gachagacha.gachagacha.item.dto.ReadBackgroundResponse;
import gachagacha.gachagacha.item.dto.UserItemResponse;
import gachagacha.gachagacha.item.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/gacha")
    public GachaResponse gacha(HttpServletRequest request) {
        return itemService.gacha(request);
    }

    @GetMapping("/items/{nickname}")
    public List<UserItemResponse> getItems(@PathVariable String nickname, @RequestParam(value = "grade", required = false) String grade, HttpServletRequest request) {
        if (grade != null) {
            return itemService.readItemsByGrade(nickname, grade, request);
        } else {
            return itemService.readAllItems(nickname, request);
        }
    }

    @GetMapping("/backgrounds/{nickname}")
    public List<ReadBackgroundResponse> readAllBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.readAllBackgrounds(nickname, request);
    }
}
