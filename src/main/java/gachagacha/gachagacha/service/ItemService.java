package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.*;
import gachagacha.gachagacha.implementation.userItem.UserItemReader;
import gachagacha.gachagacha.implementation.userItem.UserItemAppender;
import gachagacha.gachagacha.implementation.user.UserUpdator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserItemAppender userItemAppender;
    private final UserItemReader userItemReader;
    private final UserUpdator userUpdator;

    public Item gacha(User user) {
        user.deductCoinForGacha();
        Item item = Item.gacha();
        user.increaseScoreByItem(item, userItemReader.findAllByUser(user));

        userItemAppender.save(UserItem.of(user, item));
        userUpdator.update(user);
        return item;
    }

    public Page<UserItem> readUserItems(User user, Pageable pageable) {
        return userItemReader.findAllByUser(user, pageable);
    }

    public UserItem readById(long userItemId) {
        return userItemReader.findById(userItemId);
    }

    public List<UserItem> readUserItemsByItem(User user, Item item) {
        return userItemReader.findAllByUserAndItem(user, item);
    }
}
