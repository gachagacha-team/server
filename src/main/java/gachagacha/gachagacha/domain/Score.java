package gachagacha.gachagacha.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Score {

    private int score;
    private static final int INITIAL_SCORE = 0;

    public Score() {
        this.score = INITIAL_SCORE;
    }

    public void decrease(int score) {
        this.score -= score;
    }

    public void increase(int score) {
        this.score += score;
    }
}
