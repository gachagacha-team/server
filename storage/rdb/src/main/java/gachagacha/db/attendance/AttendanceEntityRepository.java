package gachagacha.db.attendance;

import gachagacha.domain.attendance.Attendance;
import gachagacha.domain.attendance.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttendanceEntityRepository implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public Optional<Attendance> findByUserIdAndDate(long userId, LocalDate date) {
        return attendanceJpaRepository.findByUserIdAndDate(userId, date)
                .map(attendanceEntity -> attendanceEntity.toAttendance());
    }

    @Override
    public void deleteAllByUserId(long userId) {
        attendanceJpaRepository.deleteAllByUserId(userId);
    }

    @Override
    public Long save(Attendance attendance) {
        AttendanceEntity attendanceEntity = attendanceJpaRepository.save(AttendanceEntity.fromAttendance(attendance));
        return attendanceEntity.getId();
    }
}
