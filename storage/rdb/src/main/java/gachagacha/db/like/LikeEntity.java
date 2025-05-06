package gachagacha.db.like;

import gachagacha.db.BaseEntity;
import gachagacha.domain.like.Like;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "likes")
public class LikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @Column(nullable = false)
    private Long minihomeId;

    @Column(nullable = false)
    private Long userId;

    public static LikeEntity fromLike(Like like) {
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.id = like.getId();
        likeEntity.minihomeId = like.getMinihomeId();
        likeEntity.userId = like.getUserId();
        return likeEntity;
    }

    public Like toLike() {
        return new Like(id, minihomeId, userId);
    }
}
