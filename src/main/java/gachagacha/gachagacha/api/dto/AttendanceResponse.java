package gachagacha.gachagacha.api.dto;

import gachagacha.gachagacha.domain.Attendance;
import gachagacha.gachagacha.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private int bonusCoin;
    private int totalCoin;

    public static AttendanceResponse of(Attendance attendance, User user) {
        return new AttendanceResponse(attendance.getBonusCoin(), user.getCoin().getCoin());
    }
}
