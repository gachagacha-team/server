package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.AttendanceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    private long id;
    private LocalDate date;
    private long userId;
    private int bonusCoin;

    public static Attendance of(LocalDate date, long userId, int bonusCoin) {
        return new Attendance(
                0l,
                date,
                userId,
                bonusCoin
        );
    }

    public AttendanceEntity toEntity() {
        return new AttendanceEntity(
                0l,
                date,
                userId,
                bonusCoin
        );
    }
}
