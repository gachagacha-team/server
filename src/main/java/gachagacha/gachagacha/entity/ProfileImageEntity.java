package gachagacha.gachagacha.entity;

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
}
