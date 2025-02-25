package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SocialTypeTest {

    @Test
    void of() {
        // given
        String githubName = "github";
        String kakaoName = "kakao";
        String invalidName = "aaa";

        // when & then
        assertThat(SocialType.of(githubName)).isEqualTo(SocialType.GITHUB);
        assertThat(SocialType.of(kakaoName)).isEqualTo(SocialType.KAKAO);
        assertThatThrownBy(() -> SocialType.of(invalidName))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_SOCIAL_TYPE.getMessage());
    }
}
