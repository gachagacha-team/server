package gachagacha.domain.item;

import gachagacha.domain.user.Profile;
import gachagacha.domain.user.SocialType;
import gachagacha.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserItemTest {

    @Test
    void 자신의_아이템인_경우() {
        User user = User.of("user", SocialType.KAKAO, 1l, Profile.BEAR);
        UserItem userItem = UserItem.of(user, Item.BLACK_CAT);

        assertThat(userItem.isOwnedBy(user)).isTrue();
    }

    @Test
    void 자신의_아이템이_아닌_경우() {
        User user1 = User.of("user1", SocialType.KAKAO, 1l, Profile.BEAR);
        User user2 = User.of("user2", SocialType.KAKAO, 2l, Profile.BEAR);
        UserItem userItem = UserItem.of(user1, Item.BLACK_CAT);

        assertThat(userItem.isOwnedBy(user2)).isTrue();
    }
}
