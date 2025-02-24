package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.domain.Attendance;
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
    private long id;

    private LocalDate date;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int bonusCoin;

    public Attendance toAttendance() {
        return new Attendance(id, date, userId, bonusCoin);
    }

    public static AttendanceEntity fromDomain(Attendance attendance) {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.date = attendance.getDate();
        attendanceEntity.userId = attendance.getUserId();
        attendanceEntity.bonusCoin = attendance.getBonusCoin();
        return attendanceEntity;
    }
}
