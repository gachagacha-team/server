package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.domain.ProfileImage;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageEntity {

    private String uploadFileName;
    private String storeFileName;

    public static ProfileImageEntity fromDomain(ProfileImage profileImage) {
        return new ProfileImageEntity(profileImage.getUploadFileName(), profileImage.getStoreFileName());
    }
}
