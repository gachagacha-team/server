package gachagacha.gachagacha.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gachagacha.gachagacha.domain.guestbook.Guestbook;
import gachagacha.gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GuestbookResponse {

    @JsonProperty(value = "guestbookId")
    private long guestbookId;

    @JsonProperty(value = "profileId")
    private long profileId;

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
                    -1,
                    "undefined",
                    guestbook.getContent(),
                    guestbook.getCreatedAt(),
                    false);
        }
        return new GuestbookResponse(
                guestbook.getId(),
                author.getProfile().getId(),
                author.getNickname(),
                guestbook.getContent(),
                guestbook.getCreatedAt(),
                author.getNickname().equals(viewUserNickname));
    }
}
