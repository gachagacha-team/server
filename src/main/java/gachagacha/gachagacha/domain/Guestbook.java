package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.GuestbookEntity;
import gachagacha.gachagacha.entity.UserEntity;
import jakarta.persistence.Column;
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

//    public static Guestbook fromEntity(GuestbookEntity guestbookEntity, UserEntity userEntity) {
//        Guestbook guestbook = new Guestbook();
//        guestbook.minihomeId = guestbookEntity.getMinihomeId();
//        guestbook.authorName = userEntity.getNickname();
//        guestbook.content = guestbookEntity.getContent();
//        guestbook.createdAt = guestbookEntity.getCreatedAt();
//        return guestbook;
//    }

    public void edit(String content) {
        this.content = content;
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
