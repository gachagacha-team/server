package gachagacha.gachagacha.user.repository;

import gachagacha.gachagacha.user.entity.Follow;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Slice<Follow> findByFollowee(User followee);
    Slice<Follow> findByFollower(User follower);

    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
}
