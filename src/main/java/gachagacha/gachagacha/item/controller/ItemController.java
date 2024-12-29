package gachagacha.gachagacha.item.controller;

import gachagacha.gachagacha.item.dto.ReadBackgroundResponse;
import gachagacha.gachagacha.item.dto.ReadAllItemResponse;
import gachagacha.gachagacha.item.dto.UserItemResponse;
import gachagacha.gachagacha.item.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/gacha")
    public String gacha(HttpServletRequest request) {
        String imageName = itemService.gacha(request);
        return "/images/items/" + imageName;
    }

    @GetMapping("/{nickname}/items")
    public List<UserItemResponse> getItems(@PathVariable String nickname, @RequestParam(value = "grade") String grade, HttpServletRequest request) {
        return itemService.getItems(nickname, grade, request);
    }

    @GetMapping("/{nickname}/allItems")
    public List<ReadAllItemResponse> getAllItems(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getAllItems(nickname, request);
    }

    @GetMapping("/{nickname}/allBackgrounds")
    public List<ReadBackgroundResponse> getAllBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getAllBackgrounds(nickname, request);
    }

    @GetMapping(value = "/image/items/{imageName}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity getItemImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/items/" + imageName);
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping(value = "/image/backgrounds/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getBackgroundImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/backgrounds/" + imageName);
        return ResponseEntity.ok().body(resource);
    }
}
