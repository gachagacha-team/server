package gachagacha.gachagacha.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Value(value = "${image.default.profile}")
    private String defaultProfileName;

    @Operation(summary = "아이템 이미지 조회")
    @Parameter(name = "imageName", description = "이미지 파일명")
    @GetMapping(value = "/image/items/{imageName}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity getItemImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/items/" + imageName);
        return ResponseEntity.ok().body(resource);
    }

    @Operation(summary = "배경 이미지 조회")
    @Parameter(name = "imageName", description = "이미지 파일명")
    @GetMapping(value = "/image/backgrounds/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getBackgroundImage(@PathVariable String imageName) {
        ClassPathResource resource = new ClassPathResource("/backgrounds/" + imageName);
        return ResponseEntity.ok().body(resource);
    }

    @Operation(summary = "프로필 이미지 조회")
    @Parameter(name = "imageName", description = "이미지 파일명")
    @GetMapping(value = "/image/profile/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getProfileImage(@PathVariable String imageName) {
        if (imageName.equals(defaultProfileName)) {
            ClassPathResource resource = new ClassPathResource(imageName);
            return ResponseEntity.ok().body(resource);
        } else {
            File imageFile = new File(System.getProperty("user.dir") + fileDir + imageName);
            Resource resource = new FileSystemResource(imageFile);
            return ResponseEntity.ok()
                    .body(resource);
        }
    }
}
