package gachagacha.gachagacha.user.service;

import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.auth.jwt.JwtDto;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.user.dto.AttendanceResponse;
import gachagacha.gachagacha.user.entity.Attendance;
import gachagacha.gachagacha.user.entity.LoginType;
import gachagacha.gachagacha.user.dto.JoinRequest;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.AttendanceRepository;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final JwtUtils jwtUtils;

    public JwtDto join(JoinRequest joinRequest) {
        LoginType loginType = LoginType.find(joinRequest.getLoginType());

        validateDuplicatedUser(loginType, joinRequest.getLoginId());
        validateDuplicatedNickname(joinRequest.getNickname());

        User user = User.create(loginType, joinRequest.getLoginId(), joinRequest.getNickname(), Minihome.create(), joinRequest.getProfileUrl());
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

    public AttendanceResponse attend(HttpServletRequest request) {
        String nickname = jwtUtils.getNicknameFromHeader(request);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateDuplicatedAttendance(user);
        user.attend();
        Attendance attendance = Attendance.create(user, LocalDate.now());
        attendanceRepository.save(attendance);
        return new AttendanceResponse(user.getCoin());
    }

    private void validateDuplicatedAttendance(User user) {
        LocalDate date = LocalDate.now();
        if (attendanceRepository.findByUserAndDate(user, date).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_ATTEND);
        }
    }
}
