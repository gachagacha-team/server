package gachagacha.gachagacha.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.gachagacha.domain.Guestbook;
import gachagacha.gachagacha.domain.User;
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

    public static GuestbookResponse of(Guestbook guestbook, User author, String viewUserNickname) {
        if (author == null) {
            return new GuestbookResponse(
                    guestbook.getId(),
                    "undefined",
                    guestbook.getContent(),
                    guestbook.getCreatedAt(),
                    false);
        }
        return new GuestbookResponse(
                guestbook.getId(),
                author.getNickname(),
                guestbook.getContent(),
                guestbook.getCreatedAt(),
                author.getNickname().equals(viewUserNickname));
    }
}
