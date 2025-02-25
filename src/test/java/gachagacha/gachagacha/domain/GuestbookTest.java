package gachagacha.gachagacha.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GuestbookTest {

    @Test
    void 방명록_수정() {
        // given
        Guestbook guestbook = Guestbook.of(1l, 1l, "수정전");

        // when
        guestbook.updateContent("수정후");

        // then
        Assertions.assertThat(guestbook.getContent()).isEqualTo("수정후");
    }
}
