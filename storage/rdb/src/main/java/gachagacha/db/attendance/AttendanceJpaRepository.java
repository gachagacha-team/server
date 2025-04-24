package gachagacha.db.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {

    Optional<AttendanceEntity> findByUserIdAndDate(long userId, LocalDate date);

    void deleteAllByUserId(long userId);
}
