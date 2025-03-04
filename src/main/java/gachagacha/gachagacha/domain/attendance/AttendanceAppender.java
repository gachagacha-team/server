package gachagacha.gachagacha.domain.attendance;

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
