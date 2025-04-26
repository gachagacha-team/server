package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.attendance.Attendance;
import gachagacha.domain.attendance.AttendanceRepository;
import gachagacha.domain.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserBackgroundRepository userBackgroundRepository;
    private final AttendanceRepository attendanceRepository;

    public User readUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }

    public User readUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public Attendance attend(User user) {
        LocalDate date = LocalDate.now();
        validateDuplicatedAttendance(user, date);

        int bonusCoin = (new Random().nextInt(5) + 1) * 1000;
        Attendance attendance = Attendance.of(date, user.getId(), bonusCoin);
        attendanceRepository.save(attendance);

        user.addCoin(bonusCoin);
        userRepository.update(user);
        return attendance;
    }

    private void validateDuplicatedAttendance(User user, LocalDate date) {
        if (attendanceRepository.findByUserIdAndDate(user.getId(), date).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_ATTEND);
        }
    }

    @Transactional
    public void updateUserInfo(User user, String nickname, long profileId) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
        user.updateUserInfo(nickname, Profile.findById(profileId));
        userRepository.update(user);
    }

    public List<Background> readUserBackgrounds(User user) {
        return userBackgroundRepository.findByUser(user);
    }
}
