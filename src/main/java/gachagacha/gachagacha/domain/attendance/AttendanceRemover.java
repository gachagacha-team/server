package gachagacha.gachagacha.domain.attendance;

import gachagacha.gachagacha.domain.user.User;
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
