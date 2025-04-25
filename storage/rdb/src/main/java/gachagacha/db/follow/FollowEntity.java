package gachagacha.db.follow;

import gachagacha.db.BaseEntity;
import gachagacha.domain.follow.Follow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "follow",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "follower_id", "followee_id" }) })
@AllArgsConstructor
@NoArgsConstructor
public class FollowEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @Column(nullable = false)
    private long followeeId;

    @Column(nullable = false)
    private long followerId;

    public static FollowEntity fromFollow(Follow follow) {
        return new FollowEntity(follow.getId(), follow.getFolloweeId(), follow.getFollowerId());
    }

    public Follow toFollow() {
        return new Follow(id, followeeId, followerId);
    }
}
