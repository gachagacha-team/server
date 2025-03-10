package gachagacha.gachagacha.dd;

import gachagacha.gachagacha.dd.dto.Decoration;
import gachagacha.gachagacha.domain.item.UserItem;
import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DecorationProcessor {

    private final RedisTemplate<String, Decoration> redisTemplate;
    private static final String DECORATION_PREFIX = "decoration:";

    public void save(Decoration decoration, long userId) {
        redisTemplate.opsForValue().set(DECORATION_PREFIX + userId, decoration);
    }

    public Decoration read(User user) {
        Decoration decoration = redisTemplate.opsForValue().get(DECORATION_PREFIX + user.getId());
        return decoration;
    }

    public boolean isUsedInMinihomeDecoration(UserItem userItem, User user) {
        Decoration decoration = redisTemplate.opsForValue().get(DECORATION_PREFIX + user.getId());
        return decoration.getItems().stream()
                .anyMatch(decorationItem -> decorationItem.getUserItemId() == userItem.getId());
    }
}
