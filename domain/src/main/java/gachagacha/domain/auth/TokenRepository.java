package gachagacha.domain.auth;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository {

    void save(String refreshToken);

    void delete(String refreshToken);

    void validateRefreshToken(String refreshToken);
}
