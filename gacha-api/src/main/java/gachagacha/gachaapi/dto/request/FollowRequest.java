package gachagacha.gachaapi.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FollowRequest {

    private String followeeUserNickname;
}
