package gachagacha.gachagacha.api.dto.response;

import gachagacha.gachagacha.domain.attendance.Attendance;
import gachagacha.gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private int bonusCoin;
    private int totalCoin;

    public static AttendanceResponse of(Attendance attendance, User user) {
        return new AttendanceResponse(attendance.getBonusCoin(), user.getCoin().getCoin());
    }
}
