package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.Minihome;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "minihome")
@NoArgsConstructor
@AllArgsConstructor
public class MinihomeEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minihome_id")
    private long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int totalVisitorCnt;

    @Lob
    private String layout;

    public static MinihomeEntity create() {
        MinihomeEntity miniHome = new MinihomeEntity();
        miniHome.totalVisitorCnt = 0;
        return miniHome;
    }

    public Minihome toMinihome() {
        return new Minihome(id, userId, totalVisitorCnt, layout);
    }

    public void updateFromMinihome(Minihome minihome) {
        this.userId = minihome.getUserId();
        this.totalVisitorCnt = minihome.getTotalVisitorCnt();
        this.layout = minihome.getLayout();
    }
}
