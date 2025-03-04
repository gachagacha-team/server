package gachagacha.gachagacha.domain.attendance;

import gachagacha.gachagacha.domain.item.entity.AttendanceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    private Long id;
    private LocalDate date;
    private long userId;
    private int bonusCoin;

    public static Attendance of(LocalDate date, long userId, int bonusCoin) {
        return new Attendance(
                null,
                date,
                userId,
                bonusCoin
        );
    }

    public AttendanceEntity toEntity() {
        return new AttendanceEntity(
                id,
                date,
                userId,
                bonusCoin
        );
    }
}
