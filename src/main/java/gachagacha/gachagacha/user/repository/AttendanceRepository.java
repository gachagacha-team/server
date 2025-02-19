package gachagacha.gachagacha.user.repository;

import gachagacha.gachagacha.user.entity.Attendance;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserAndDate(User user, LocalDate date);

    List<Attendance> findByUser(User user);
}
