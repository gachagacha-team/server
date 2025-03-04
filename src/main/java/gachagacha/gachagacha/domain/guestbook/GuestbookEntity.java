package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.domain.BaseEntity;
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
    private Long id;

    @Column(nullable = false)
    private Long minihomeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    public Guestbook toGuestbook() {
        return new Guestbook(id, minihomeId, userId, content, getCreatedAt());
    }

    public void updateFromGuestbook(Guestbook guestbook) {
        this.minihomeId = guestbook.getMinihomeId();
        this.userId = guestbook.getUserId();
        this.content = guestbook.getContent();
    }
}
