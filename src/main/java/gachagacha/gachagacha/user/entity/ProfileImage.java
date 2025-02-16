package gachagacha.gachagacha.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class ProfileImage {

    private String uploadFileName;
    private String storeFileName;

    public static ProfileImage create(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        String storeFileName = UUID.randomUUID().toString() + "." + ext;

        ProfileImage profileImage = new ProfileImage();
        profileImage.uploadFileName = originalFilename;
        profileImage.storeFileName = storeFileName;
        return profileImage;
    }
}
