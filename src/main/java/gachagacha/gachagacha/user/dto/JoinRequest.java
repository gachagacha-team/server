package gachagacha.gachagacha.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {

    private String nickname;
    private String loginType;
    private Long loginId;
}
