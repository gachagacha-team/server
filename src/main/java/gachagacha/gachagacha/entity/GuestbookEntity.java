package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.Guestbook;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "guestbook")
@NoArgsConstructor
@AllArgsConstructor
public class GuestbookEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private long id;

    @Column(nullable = false)
    private long minihomeId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String content;

    public static GuestbookEntity fromDomain(Guestbook guestbook) {
        GuestbookEntity guestbookEntity = new GuestbookEntity();
        guestbookEntity.minihomeId = guestbook.getMinihomeId();
        guestbookEntity.userId = guestbook.getUserId();
        guestbookEntity.content = guestbook.getContent();
        return guestbookEntity;
    }

    public Guestbook toGuestbook() {
        return new Guestbook(id, minihomeId, userId, content, getCreatedAt());
    }

    public void editContent(String content) {
        this.content = content;
    }

    public void updateFromGuestbook(Guestbook guestbook) {
        this.minihomeId = guestbook.getMinihomeId();
        this.userId = guestbook.getUserId();
        this.content = guestbook.getContent();
    }
}
