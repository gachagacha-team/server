package gachagacha.db.minihome;

import gachagacha.db.BaseEntity;
import gachagacha.domain.minihome.Minihome;
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

    public static MinihomeEntity fromMinihome(Minihome minihome) {
        return new MinihomeEntity(minihome.getId(), minihome.getUserId(), minihome.getTotalVisitorCnt());
    }

    public void updateFromMinihome(Minihome minihome) {
        this.userId = minihome.getUserId();
        this.totalVisitorCnt = minihome.getTotalVisitorCnt();
    }
}
