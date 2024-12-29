package gachagacha.gachagacha.item.controller;

import gachagacha.gachagacha.item.dto.AddItemResponse;
import gachagacha.gachagacha.item.dto.ReadBackgroundResponse;
import gachagacha.gachagacha.item.dto.ReadItemResponse;
import gachagacha.gachagacha.item.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{nickname}/backgrounds")
    public List<ReadBackgroundResponse> getBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getBackgrounds(nickname, request);
    }

    @GetMapping("/{nickname}/items")
    public List<ReadItemResponse> getItems(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getItems(nickname, request);
    }

    @GetMapping(value = "/images/items/{imageName}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity getItemImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/items/" + imageName);
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping(value = "/images/backgrounds/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getBackgroundImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/backgrounds/" + imageName);
        return ResponseEntity.ok().body(resource);
    }
}
