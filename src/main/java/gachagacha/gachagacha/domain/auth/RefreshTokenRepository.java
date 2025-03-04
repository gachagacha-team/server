package gachagacha.gachagacha.domain.auth;

import gachagacha.gachagacha.domain.item.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expires_at <= :currentTime")
    @Transactional
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}
