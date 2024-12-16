package gachagacha.gachagacha.minihome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddGuestbookResponse {

    private long guestbookId;
    private String guestbookUserNickname;
    private String content;
}
