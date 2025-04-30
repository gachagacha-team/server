package gachagacha.domain.minihome;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Minihome {

    private Long id;
    private Long userId;
    private int totalVisitorCnt;

    public static Minihome createInitialMinihome(Long userId) {
        return new Minihome(null, userId, 0);
    }

    public void visit() {
        this.totalVisitorCnt++;
    }
}
