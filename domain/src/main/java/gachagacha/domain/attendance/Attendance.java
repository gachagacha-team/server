package gachagacha.domain.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    private Long id;
    private LocalDate date;
    private long userId;
    private int bonusCoin;
}
