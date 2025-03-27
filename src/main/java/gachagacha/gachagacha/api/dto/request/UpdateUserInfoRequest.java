package gachagacha.gachagacha.api.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserInfoRequest {

    private String nickname;
    private long profileId;
}
