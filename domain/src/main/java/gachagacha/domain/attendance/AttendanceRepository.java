package gachagacha.domain.attendance;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository {

    Optional<Attendance> findByUserIdAndDate(long userId, LocalDate date);

    void deleteAllByUserId(long userId);

    Long save(Attendance attendance);
}
