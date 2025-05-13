package gachagacha.db.minihome;

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
}
