package gachagacha.domain.lotto;

import gachagacha.domain.item.ItemGrade;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LottoTest {

    @Test
    void 복권_사용() {
        Lotto lotto = Lotto.createInitialLotto(1l, ItemGrade.A, true, 1000);
        assertThat(lotto.isUsed()).isFalse();

        lotto.use();
        assertThat(lotto.isUsed()).isTrue();
    }
}
