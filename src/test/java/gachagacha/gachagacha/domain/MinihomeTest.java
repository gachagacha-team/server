package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.domain.minihome.Minihome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MinihomeTest {

    @Test
    void 미니홈_방문시_방문자수가_1_증가한다() {
        // given
        Minihome minihome = Minihome.of(1l);
        int beforeVisit = minihome.getTotalVisitorCnt();

        // when
        minihome.visit();

        // then
        assertThat(minihome.getTotalVisitorCnt()).isEqualTo(beforeVisit + 1);
    }
}
