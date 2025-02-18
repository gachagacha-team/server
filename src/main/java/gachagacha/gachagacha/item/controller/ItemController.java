package gachagacha.gachagacha.item.controller;

import gachagacha.gachagacha.item.dto.GachaResponse;
import gachagacha.gachagacha.item.dto.ReadBackgroundResponse;
import gachagacha.gachagacha.item.dto.UserItemResponse;
import gachagacha.gachagacha.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 뽑기")
    @PostMapping("/gacha")
    public GachaResponse gacha(HttpServletRequest request) {
        return itemService.gacha(request);
    }

    @Operation(summary = "사용자가 보유한 아이템 리스트 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @Parameter(name = "grade", description = "조회할 아이템 등급(S, A, B, C, D)(생략 시 모든 아이템 조회)")
    @GetMapping("/items/{nickname}")
    public List<UserItemResponse> getItems(@PathVariable String nickname, @RequestParam(value = "grade", required = false) String grade, HttpServletRequest request) {
        if (grade != null) {
            return itemService.readItemsByGrade(nickname, grade, request);
        } else {
            return itemService.readAllItems(nickname, request);
        }
    }

    @Operation(summary = "사용자가 보유한 배경 리스트 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/backgrounds/{nickname}")
    public List<ReadBackgroundResponse> readAllBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.readAllBackgrounds(nickname, request);
    }
}
