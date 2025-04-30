package gachagacha.domain.minihome;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MinihomeTest {

    @Test
    void 미니홈_방문시_방문자_수_1_증가() {
        Minihome minihome = Minihome.of(1l);
        assertThat(minihome.getTotalVisitorCnt()).isEqualTo(0);

        minihome.visit();
        assertThat(minihome.getTotalVisitorCnt()).isEqualTo(1);
    }
}
