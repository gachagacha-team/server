package gachagacha.gachagacha.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class JoinRequest {

    private String nickname;
    private String socialType;
    private Long loginId;
}
