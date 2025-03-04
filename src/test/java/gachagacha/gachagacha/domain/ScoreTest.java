package gachagacha.gachagacha.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreTest {

    @Test
    void 스코어_초기값은_0이다() {
        // given
        Score score = new Score();

        // when & then
        Assertions.assertThat(score.getScore()).isEqualTo(0);
    }

    @Test
    void 스코어_감소() {
        // given
        Score score = new Score();
        int initialScore = score.getScore();

        // when
        score.decrease(1000);

        // then
        Assertions.assertThat(score.getScore()).isEqualTo(initialScore - 1000);
    }

    @Test
    void 스코어_증가() {
        // given
        Score score = new Score();
        int initialScore = score.getScore();

        // when
        score.increase(1000);

        // then
        Assertions.assertThat(score.getScore()).isEqualTo(initialScore + 1000);
    }
}
