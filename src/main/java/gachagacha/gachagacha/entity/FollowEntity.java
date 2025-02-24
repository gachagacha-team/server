package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.Follow;
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
    private long id;

    @Column(nullable = false)
    private long followeeId;

    @Column(nullable = false)
    private long followerId;

    public static FollowEntity create(long followeeId, long followerId) {
        FollowEntity follow = new FollowEntity();
        follow.followeeId = followeeId;
        follow.followerId = followerId;
        return follow;
    }

    public Follow toFollow() {
        return new Follow(id, followeeId, followerId);
    }
}
