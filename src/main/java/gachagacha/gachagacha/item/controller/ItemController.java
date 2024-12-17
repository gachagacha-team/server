package gachagacha.gachagacha.item.controller;

import gachagacha.gachagacha.item.dto.AddBackgroundResponse;
import gachagacha.gachagacha.item.dto.AddItemResponse;
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

    @PostMapping("/item/{itemTypeId}")
    public AddItemResponse addItem(@PathVariable int itemTypeId, HttpServletRequest request) {
        return itemService.addItem(itemTypeId, request);
    }

    @GetMapping("/minihome/{nickname}/items")
    public List<String> getItems(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getItems(nickname, request);
    }

    @GetMapping(value = "/image/items/{imageName}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity getItemImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/items/" + imageName);
        return ResponseEntity.ok().body(resource);
    }

    @PostMapping("/background/{backgroundTypeId}")
    public AddBackgroundResponse addBackground(@PathVariable int backgroundTypeId, HttpServletRequest request) {
        return itemService.addBackground(backgroundTypeId, request);
    }

    @GetMapping("/minihome/{nickname}/backgrounds")
    public List<String> getBackgrounds(@PathVariable String nickname, HttpServletRequest request) {
        return itemService.getBackgrounds(nickname, request);
    }

    @GetMapping(value = "/image/backgrounds/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getBackgroundImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/backgrounds/" + imageName);
        return ResponseEntity.ok().body(resource);
    }
}
