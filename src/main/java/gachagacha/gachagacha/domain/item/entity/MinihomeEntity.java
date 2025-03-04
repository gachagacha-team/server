package gachagacha.gachagacha.domain.item.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.minihome.Minihome;
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
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int totalVisitorCnt;

    @Lob
    private String layout;

    public Minihome toMinihome() {
        return new Minihome(id, userId, totalVisitorCnt, layout);
    }

    public void updateFromMinihome(Minihome minihome) {
        this.userId = minihome.getUserId();
        this.totalVisitorCnt = minihome.getTotalVisitorCnt();
        this.layout = minihome.getLayout();
    }
}
