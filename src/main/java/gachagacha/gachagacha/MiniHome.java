package gachagacha.gachagacha;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MiniHome extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    private long id;

    @Column(nullable = false)
    private int totalVisitorCnt;

    public static MiniHome create() {
        MiniHome miniHome = new MiniHome();
        miniHome.totalVisitorCnt = 0;
        return miniHome;
    }
}
