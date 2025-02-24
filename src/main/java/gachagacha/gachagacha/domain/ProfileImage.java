package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.ProfileImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProfileImage {

    private String uploadFileName;
    private String storeFileName;

    public static ProfileImage of(MultipartFile file) {
        if (file == null) {
            return new ProfileImage(null, "default_profile.png");
        } else {
            String originalFilename = file.getOriginalFilename();
            int pos = originalFilename.lastIndexOf(".");
            String ext = originalFilename.substring(pos + 1);
            String storeFileName = UUID.randomUUID() + "." + ext;
            return new ProfileImage(originalFilename, storeFileName);
        }
    }

    public ProfileImageEntity toEntity() {
        return new ProfileImageEntity(uploadFileName, storeFileName);
    }
}
