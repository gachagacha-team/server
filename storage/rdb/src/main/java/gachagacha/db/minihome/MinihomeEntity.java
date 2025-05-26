package gachagacha.db.minihome;

import gachagacha.db.BaseEntity;
import gachagacha.domain.minihome.Minihome;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "minihome",
        indexes = {
                @Index(name = "idx_minihome_created_at_minihome_id", columnList = "created_at, minihome_id"),
                @Index(name = "idx_minihome_total_visitor_cnt_minihome_id", columnList = "total_visitor_cnt, minihome_id")
        })
public class MinihomeEntity extends BaseEntity {

    @Column(name = "minihome_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
