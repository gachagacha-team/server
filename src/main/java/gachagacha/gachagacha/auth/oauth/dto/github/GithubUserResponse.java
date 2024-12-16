package gachagacha.gachagacha.auth.oauth.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubUserResponse {

    @JsonProperty("loginWithGithub")
    private String login;

    @JsonProperty("id")
    private Long id;
}
