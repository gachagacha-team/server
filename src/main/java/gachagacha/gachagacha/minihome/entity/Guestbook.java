package gachagacha.gachagacha.minihome.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Guestbook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "minihome_id")
    private Minihome minihome;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    public void setMiniHome(Minihome minihome) {
        this.minihome = minihome;
    }

    public static Guestbook create(User user, String content) {
        Guestbook guestbook = new Guestbook();
        guestbook.user = user;
        guestbook.content = content;
        return guestbook;
    }

    public void edit(String content) {
        this.content = content;
    }

    public void softDeleteByMinihomeUser() {
        minihome = null;
    }

    public void softDeleteByUser() {
        user = null;
    }
}
