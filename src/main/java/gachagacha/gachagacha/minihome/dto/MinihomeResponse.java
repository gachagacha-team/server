package gachagacha.gachagacha.minihome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MinihomeResponse {

    private String nickname;
    private int coin;
    private int totalVisitorCnt;
    private String profileImageUrl;
    private String layout;
}
