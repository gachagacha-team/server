package gachagacha.gachagacha.auth;

import gachagacha.gachagacha.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expires_at <= :currentTime")
    @Transactional
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}
