package gachagacha.db.user;

import gachagacha.domain.user.SocialType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Slice<UserEntity> findAllBy(Pageable pageable);

    Optional<UserEntity> findBySocialTypeAndLoginId(@Param(value = "socialType") SocialType socialType, @Param(value = "loginId") Long loginId);

    Optional<UserEntity> findByNickname(@Param(value = "nickname") String nickname);
}
