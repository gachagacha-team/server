package gachagacha.db.meta;

import gachagacha.domain.meta.MinihomeMeta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "minihome_meta")
@NoArgsConstructor
@AllArgsConstructor
public class MinihomeMetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minihome_meta_id")
    private Long id;

    @Column(nullable = false)
    private Long minihomeId;

    @Column(nullable = false)
    private Long likeCount;

    public static MinihomeMetaEntity fromMinihomeMeta(MinihomeMeta minihomeMeta) {
        return new MinihomeMetaEntity(minihomeMeta.getId(), minihomeMeta.getMinihomeId(), minihomeMeta.getLikeCount());
    }
}
