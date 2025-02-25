package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.Attendance;
import gachagacha.gachagacha.domain.Coin;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.implementation.attendance.AttendanceAppender;
import gachagacha.gachagacha.implementation.attendance.AttendanceReader;
import gachagacha.gachagacha.implementation.user.UserReader;
import gachagacha.gachagacha.implementation.user.UserUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserUpdater userUpdater;
    private final AttendanceAppender attendanceAppender;
    private final AttendanceReader attendanceReader;

    public User readUserByNickname(String nickname) {
        return userReader.findByNickname(nickname);
    }

    public User readUserById(long userId) {
        return userReader.findById(userId);
    }

    public Coin getCoin(String nickname) {
        User user = userReader.findByNickname(nickname);
        return user.getCoin();
    }

    @Transactional
    public Attendance attend(User user) {
        LocalDate date = LocalDate.now();
        validateDuplicatedAttendance(user, date);

        int bonusCoin = (new Random().nextInt(5) + 1) * 1000;
        Attendance attendance = Attendance.of(date, user.getId(), bonusCoin);
        attendanceAppender.save(attendance);

        user.addCoin(bonusCoin);
        userUpdater.update(user);
        return attendance;
    }

    private void validateDuplicatedAttendance(User user, LocalDate date) {
        if (attendanceReader.existsByUserAndDate(user, date)) {
            throw new BusinessException(ErrorCode.ALREADY_ATTEND);
        }
    }
}
