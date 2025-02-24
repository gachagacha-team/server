package gachagacha.gachagacha.implementation.attendance;

import gachagacha.gachagacha.repository.AttendanceRepository;
import gachagacha.gachagacha.domain.User;
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
