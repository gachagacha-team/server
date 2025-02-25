package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.GuestbookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Guestbook {

    private long id;
    private long minihomeId;
    private long userId;
    private String content;
    private LocalDateTime createdAt;

    public static Guestbook of(long minihomeId, long userId, String content) {
        return new Guestbook(
                0l,
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
