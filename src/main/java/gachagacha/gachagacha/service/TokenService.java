package gachagacha.gachagacha.service;

import gachagacha.gachagacha.repository.RefreshTokenRepository;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.RefreshTokenEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.implementation.user.UserReader;
import gachagacha.gachagacha.jwt.JwtDto;
import gachagacha.gachagacha.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserReader userReader;

    public void save(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void save(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public JwtDto renewTokens(HttpServletRequest request) {
        jwtUtils.validateTokenFromHeader(request);

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(jwtUtils.getRefreshTokenFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT));

        refreshTokenRepository.delete(refreshTokenEntity);
        User user = userReader.findByNickname(jwtUtils.getUserNicknameFromHeader(request));
        JwtDto jwtDto = jwtUtils.generateJwt(user);
        refreshTokenRepository.save(new RefreshTokenEntity(jwtDto.getRefreshToken()));
        return jwtDto;
    }
}
