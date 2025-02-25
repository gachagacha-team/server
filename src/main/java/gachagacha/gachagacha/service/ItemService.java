package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.*;
import gachagacha.gachagacha.implementation.userItem.UserItemReader;
import gachagacha.gachagacha.implementation.userItem.UserItemAppender;
import gachagacha.gachagacha.implementation.user.UserUpdater;
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
