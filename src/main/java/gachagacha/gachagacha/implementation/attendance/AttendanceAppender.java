package gachagacha.gachagacha.implementation.attendance;

import gachagacha.gachagacha.repository.AttendanceRepository;
import gachagacha.gachagacha.domain.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceAppender {

    private final AttendanceRepository attendanceRepository;

    public void save(Attendance attendance) {
        attendanceRepository.save(attendance.toEntity());
    }
}
