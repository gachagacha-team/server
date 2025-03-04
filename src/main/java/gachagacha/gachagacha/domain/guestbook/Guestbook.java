package gachagacha.gachagacha.domain.guestbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Guestbook {

    private Long id;
    private Long minihomeId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    public static Guestbook of(long minihomeId, long userId, String content) {
        return new Guestbook(
                null,
                minihomeId,
                userId,
                content,
                null
        );
    }

    public void setId(long id) {
        this.id = id;
    }

    public GuestbookEntity toGuestbookEntity() {
        return new GuestbookEntity(
                id,
                minihomeId,
                userId,
                content
        );
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
