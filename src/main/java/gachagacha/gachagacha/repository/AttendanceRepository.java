package gachagacha.gachagacha.repository;

import gachagacha.gachagacha.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    Optional<AttendanceEntity> findByUserIdAndDate(long userId, LocalDate date);

    void deleteAllByUserId(long userId);
}
