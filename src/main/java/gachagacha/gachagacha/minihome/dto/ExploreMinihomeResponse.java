package gachagacha.gachagacha.minihome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExploreMinihomeResponse {

    private String nickname;
    private int totalVisitorCnt;
    private String profileImageStoreFileName;
}
