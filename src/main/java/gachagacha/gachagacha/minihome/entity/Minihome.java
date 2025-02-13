package gachagacha.gachagacha.minihome.entity;

import gachagacha.gachagacha.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Minihome extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minihome_id")
    private long id;

    @Column(nullable = false)
    private int totalVisitorCnt;

    @OneToMany(mappedBy = "minihome", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guestbook> guestbooks = new ArrayList<>();

    @Lob
    private String layout;

    public static Minihome create() {
        Minihome miniHome = new Minihome();
        miniHome.totalVisitorCnt = 0;
        return miniHome;
    }

    public void addGuestbook(Guestbook guestbook) {
        this.guestbooks.add(guestbook);
        if (guestbook.getMinihome() != null) {
            guestbook.getMinihome().getGuestbooks().remove(guestbook);
        }
        guestbook.setMiniHome(this);
    }

    public void visit() {
        this.totalVisitorCnt++;
    }
}
