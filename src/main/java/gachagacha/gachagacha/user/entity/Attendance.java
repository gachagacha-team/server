package gachagacha.gachagacha.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Attendance create(User user, LocalDate date) {
        Attendance attendance = new Attendance();
        attendance.date = date;
        attendance.user = user;
        return attendance;
    }
}
