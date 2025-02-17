package gachagacha.gachagacha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ImageController {

    @Value("${file.profile}")
    private String fileDir;

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

    @GetMapping(value = "/image/profile/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getProfileImage(@PathVariable String imageName) {
        File imageFile = new File(System.getProperty("user.dir") + fileDir + imageName);
        Resource resource = new FileSystemResource(imageFile);
        return ResponseEntity.ok()
                .body(resource);
    }
}
