package gachagacha.gachagacha.item.controller;

import gachagacha.gachagacha.item.dto.AddItemResponse;
import gachagacha.gachagacha.item.dto.ReadItemsResponse;
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

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/item/{itemId}")
    public AddItemResponse addItem(@PathVariable int itemId, HttpServletRequest request) {
        return itemService.addItem(itemId, request);
    }

    @GetMapping("/{nickname}/items")
    public ReadItemsResponse getItems(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getItems(nickname, request);
    }

    @GetMapping(value = "/image/characters/{imageName}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity getItemImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/characters/" + imageName);
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping(value = "/image/backgrounds/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getBackgroundImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/backgrounds/" + imageName);
        return ResponseEntity.ok().body(resource);
    }
}
