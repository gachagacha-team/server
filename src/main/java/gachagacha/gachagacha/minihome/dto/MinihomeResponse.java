package gachagacha.gachagacha.minihome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MinihomeResponse {

    private String nickname;
    private int coin;
    private int totalVisitorCnt;
    private String layout;
}
