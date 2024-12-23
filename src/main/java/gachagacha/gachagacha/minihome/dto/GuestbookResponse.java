package gachagacha.gachagacha.minihome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuestbookResponse {
    private long guestbookId;
    private String nickname;
    private String content;
    private LocalDateTime createAt;
    private boolean isAuthor;
}
