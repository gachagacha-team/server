package gachagacha.gachaapi.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class AddGuestbookRequest {

    private String content;
}
