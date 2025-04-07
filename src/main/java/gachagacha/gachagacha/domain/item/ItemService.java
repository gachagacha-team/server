package gachagacha.gachagacha.domain.item;

import gachagacha.gachagacha.domain.decoration.Decoration;
import gachagacha.gachagacha.domain.decoration.DecorationProcessor;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserItemAppender userItemAppender;
    private final UserItemReader userItemReader;
    private final UserUpdater userUpdater;
    private final DecorationProcessor decorationProcessor;

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

    public List<UserItem> readAllUserItems(User user) {
        return userItemReader.findAllByUser(user);
    }

    public List<UserItem> readUserItemsExcludeDecorationItem(User user) {
        List<UserItem> userItems = userItemReader.findAllByUser(user);
        Decoration decoration = decorationProcessor.read(user);
        if (decoration.getItems() == null) {
            return userItems;
        }

        Set<Long> decorationUserItems = decoration.getItems().stream()
                .map(Decoration.DecorationItem::getUserItemId)
                .collect(Collectors.toSet());

        return userItems.stream()
                .filter(userItem -> !decorationUserItems.contains(userItem.getId()))
                .toList();
    }
}
