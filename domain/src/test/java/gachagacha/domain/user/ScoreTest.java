package gachagacha.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreTest {


    @Test
    void 최초_초기화되는_스코어는_0() {
        Score score = Score.createInitialScore();

        assertThat(score.getScore()).isEqualTo(0);
    }

    @Test
    void 스코어_증가() {
        Score score = Score.createInitialScore();
        int initialScoreAmount = score.getScore();

        score.increase(1000);

        assertThat(score.getScore()).isEqualTo(initialScoreAmount + 1000);
    }

    @Test
    void 스코어_감소() {
        Score score = Score.createInitialScore();
        int initialScoreAmount = score.getScore();

        score.decrease(1000);

        assertThat(score.getScore()).isEqualTo(initialScoreAmount - 1000);
    }
}
