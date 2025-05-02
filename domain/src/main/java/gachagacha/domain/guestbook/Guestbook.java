package gachagacha.domain.guestbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Guestbook {

    private Long id;
    private Long minihomeId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    public static Guestbook createInitialGuestbook(Long minihomeId, Long userId, String content) {
        return new Guestbook(null, minihomeId, userId, content, null);
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
