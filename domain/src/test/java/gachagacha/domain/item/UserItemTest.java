package gachagacha.domain.item;

import gachagacha.domain.user.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserItemTest {

    @Test
    void 자신의_아이템인_경우() {
        User user = User.createInitialUser(SocialType.KAKAO, 1l, "user", Profile.COW);
        UserItem userItem = new UserItem(1l, Item.BLACK_CAT, user.getId());

        assertThat(userItem.isOwnedBy(user)).isTrue();
    }

    @Test
    void 자신의_아이템이_아닌_경우() {
        User user1 = mock(User.class);
        when(user1.getId()).thenReturn(1l);

        User user2 = mock(User.class);
        when(user2.getId()).thenReturn(2l);

        UserItem userItem = new UserItem(1l, Item.BLACK_CAT, user1.getId());

        assertThat(userItem.isOwnedBy(user2)).isFalse();
    }
}
