package gachagacha.gachagacha;

import gachagacha.gachagacha.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenExpirationScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredRefreshToken() {
        log.info("RefreshTokenExpirationScheduler 스케줄러 동작");
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now().withNano(0));
    }
}
