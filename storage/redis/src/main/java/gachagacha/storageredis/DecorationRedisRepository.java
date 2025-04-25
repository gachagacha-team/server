package gachagacha.storageredis;

import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.decoration.DecorationRepository;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DecorationRedisRepository implements DecorationRepository {

    private final RedisTemplate<String, Decoration> redisTemplate;
    private static final String DECORATION_PREFIX = "decoration:";

    @Override
    public void save(Decoration decoration, long userId) {
        redisTemplate.opsForValue().set(DECORATION_PREFIX + userId, decoration);
    }

    @Override
    public Decoration read(User user) {
        Decoration decoration = redisTemplate.opsForValue().get(DECORATION_PREFIX + user.getId());
        return decoration;
    }

    @Override
    public boolean isUsedInMinihomeDecoration(UserItem userItem, User user) {
        Decoration decoration = redisTemplate.opsForValue().get(DECORATION_PREFIX + user.getId());
        if (decoration.getItems() == null) {
            return false;
        }
        return decoration.getItems().stream()
                .anyMatch(decorationItem -> decorationItem.getUserItemId() == userItem.getId());
    }
}
