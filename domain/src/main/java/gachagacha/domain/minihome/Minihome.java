package gachagacha.domain.minihome;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Minihome {

    private Long id;
    private Long userId;
    private int totalVisitorCnt;
    private long likeCount;
}
