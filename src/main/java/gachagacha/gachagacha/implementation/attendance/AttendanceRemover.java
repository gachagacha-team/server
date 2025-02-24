package gachagacha.gachagacha.implementation.attendance;

import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceRemover {

    private final AttendanceRepository attendanceRepository;

    public void deleteByUser(User user) {
        attendanceRepository.deleteAllByUserId(user.getId());
    }
}
