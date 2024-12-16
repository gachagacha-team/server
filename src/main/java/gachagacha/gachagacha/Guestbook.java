package gachagacha.gachagacha;

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
    @JoinColumn(name = "home_id", nullable = false)
    private MiniHome miniHome;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;
}
