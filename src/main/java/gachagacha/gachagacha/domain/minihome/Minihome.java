package gachagacha.gachagacha.domain.minihome;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Minihome {

    private Long id;
    private Long userId;
    private int totalVisitorCnt;
    private String layout;

    public static Minihome of(long userId) {
        return new Minihome(
                null,
                userId,
                0,
                null
        );
    }

    public void visit() {
        this.totalVisitorCnt++;
    }

    public MinihomeEntity toMinihomeEntity() {
        return new MinihomeEntity(
                id,
                userId,
                totalVisitorCnt,
                layout
        );
    }
}
