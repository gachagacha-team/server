package gachagacha.domain.minihome;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Minihome {

    private Long id;
    private Long userId;
    private int totalVisitorCnt;

    public static Minihome of(long userId) {
        return new Minihome(
                null,
                userId,
                0
        );
    }

    public void visit() {
        this.totalVisitorCnt++;
    }
}
