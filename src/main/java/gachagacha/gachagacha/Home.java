package gachagacha.gachagacha;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Home extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    private long id;

    @Column(nullable = false)
    private int totalVisitorCnt;
}
