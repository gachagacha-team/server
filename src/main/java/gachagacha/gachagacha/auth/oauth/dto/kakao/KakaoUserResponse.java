package gachagacha.gachagacha.auth.oauth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class KakaoUserResponse {

    @JsonProperty("id")
    private Long id;
}
