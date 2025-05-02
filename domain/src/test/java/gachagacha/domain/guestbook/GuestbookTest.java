package gachagacha.domain.guestbook;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuestbookTest {

    @Test
    void updateContent() {
        Guestbook guestbook = Guestbook.createInitialGuestbook(1l, 1l, "변경 전 방명록");
        String contentAfterUpdate = "변경 후 방명록";
        guestbook.updateContent(contentAfterUpdate);
        assertThat(guestbook.getContent()).isEqualTo(contentAfterUpdate);
    }
}
