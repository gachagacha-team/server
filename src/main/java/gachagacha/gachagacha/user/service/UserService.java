package gachagacha.gachagacha.user.service;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.user.dto.JoinRequest;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public JwtDto join(JoinRequest joinRequest) {
        LoginType loginType = LoginType.find(joinRequest.getLoginType());

        validateDuplicatedUser(loginType, joinRequest.getLoginId());
        validateDuplicatedNickname(joinRequest.getNickname());

        User user = User.create(loginType, joinRequest.getLoginId(), joinRequest.getNickname(), Minihome.create());
        userRepository.save(user);
        return jwtUtils.generateJwt(user.getNickname());
    }

    private void validateDuplicatedUser(LoginType loginType, long loginId) {
        if (userRepository.findByLoginTypeAndLoginId(loginType, loginId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER_REGISTRATION);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }
}
