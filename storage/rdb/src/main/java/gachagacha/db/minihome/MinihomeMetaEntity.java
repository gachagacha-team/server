package gachagacha.db.minihome;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "minihome_meta", indexes = @Index(name = "idx_minihome_meta_like_count_minihome_id", columnList = "like_count, minihome_id"))
public class MinihomeMetaEntity {

    @Column(name = "minihome_meta_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long minihomeId;

    @Column(nullable = false)
    private Long likeCount;
}
