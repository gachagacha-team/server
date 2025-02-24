package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.MinihomeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Minihome {

    private long id;
    private long userId;
    private int totalVisitorCnt;
    private String layout;

    public static Minihome of(long userId) {
        return new Minihome(
                0l,
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
