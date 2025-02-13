package gachagacha.gachagacha.minihome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class GuestbookResponse {

    @JsonProperty(value = "guestbookId")
    private long guestbookId;

    @JsonProperty(value = "nickname")
    private String nickname;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "createAt")
    private LocalDateTime createAt;

    @JsonProperty(value = "isAuthor")
    private boolean isAuthor;
}
