package gachagacha.db.attendance;

import gachagacha.domain.attendance.Attendance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "attendance")
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    private LocalDate date;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int bonusCoin;

    public static AttendanceEntity fromAttendance(Attendance attendance) {
        return new AttendanceEntity(attendance.getId(), attendance.getDate(), attendance.getUserId(), attendance.getBonusCoin());
    }

    public Attendance toAttendance() {
        return new Attendance(id, date, userId, bonusCoin);
    }
}
