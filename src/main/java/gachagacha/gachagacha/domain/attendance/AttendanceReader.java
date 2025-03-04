package gachagacha.gachagacha.domain.attendance;

import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AttendanceReader {

    private final AttendanceRepository attendanceRepository;

    public boolean existsByUserAndDate(User user, LocalDate date) {
        return attendanceRepository.findByUserIdAndDate(user.getId(), date).isPresent();
    }
}
