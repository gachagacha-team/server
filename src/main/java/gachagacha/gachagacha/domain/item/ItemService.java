package gachagacha.gachagacha.domain.item;

import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItem;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.item.UserItemReader;
import gachagacha.gachagacha.domain.item.UserItemAppender;
import gachagacha.gachagacha.domain.user.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserItemAppender userItemAppender;
    private final UserItemReader userItemReader;
    private final UserUpdater userUpdater;

    @Transactional
    public Item gacha(User user) {
        user.deductCoinForGacha();
        Item item = Item.gacha();
        user.increaseScoreByItem(item, userItemReader.findAllByUser(user));

        userItemAppender.save(UserItem.of(user, item));
        userUpdater.update(user);
        return item;
    }

    public UserItem readById(long userItemId) {
        return userItemReader.findById(userItemId);
    }

    public List<UserItem> readUserItemsByItem(User user, Item item) {
        return userItemReader.findAllByUserAndItem(user, item);
    }
}
