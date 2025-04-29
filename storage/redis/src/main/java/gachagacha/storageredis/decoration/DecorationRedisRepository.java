package gachagacha.storageredis.decoration;

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

    private final RedisTemplate<String, DecorationRedisDto> redisTemplate;
    private static final String DECORATION_PREFIX = "decoration:";

    @Override
    public void save(Decoration decoration, long userId) {
        DecorationRedisDto decorationRedisDto = DecorationRedisDto.fromDecoration(decoration);
        redisTemplate.opsForValue().set(DECORATION_PREFIX + userId, decorationRedisDto);
    }

    @Override
    public Decoration read(User user) {
        DecorationRedisDto decorationRedisDto = redisTemplate.opsForValue().get(DECORATION_PREFIX + user.getId());
        return decorationRedisDto.toDecoration(user);
    }

    @Override
    public boolean isUsedInMinihomeDecoration(UserItem userItem, User user) {
        DecorationRedisDto decorationRedisDto = redisTemplate.opsForValue().get(DECORATION_PREFIX + user.getId());
        if (decorationRedisDto.getItems() == null) {
            return false;
        }
        return decorationRedisDto.getItems().stream()
                .anyMatch(decorationItem -> decorationItem.getUserItemId() == userItem.getId());
    }
}
