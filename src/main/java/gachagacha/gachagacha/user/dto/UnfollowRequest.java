package gachagacha.gachagacha.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UnfollowRequest {

    private long followeeUserId;
}
