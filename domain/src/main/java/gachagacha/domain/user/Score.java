package gachagacha.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Score {

    private int score;
    private static final int INITIAL_SCORE = 0;

    public static Score createInitialScore() {
        return new Score(INITIAL_SCORE);
    }

    public void decrease(int score) {
        this.score -= score;
    }

    public void increase(int score) {
        this.score += score;
    }
}
