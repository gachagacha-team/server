package gachagacha.gachagacha.user.repository;

import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginTypeAndLoginId(@Param(value = "loginType") LoginType loginType, @Param(value = "loginId") Long loginId);

    Optional<User> findByNickname(@Param(value = "nickname") String nickname);

    @Query("select u from User u " +
            "where u.miniHome = :minihome")
    Optional<User> findByMinihome(@Param(value = "minihome") Minihome minihome);
}
