package gachagacha.domain.attendance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
}
