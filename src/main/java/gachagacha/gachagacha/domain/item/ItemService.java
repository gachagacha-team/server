package gachagacha.gachagacha.domain.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.gachagacha.domain.decoration.Decoration;
import gachagacha.gachagacha.domain.decoration.DecorationProcessor;
import gachagacha.gachagacha.domain.outbox.LottoIssuanceEvent;
import gachagacha.gachagacha.domain.outbox.Outbox;
import gachagacha.gachagacha.domain.outbox.OutboxProcessor;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final OutboxProcessor outboxProcessor;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.stream.lotto-issuance-requests}")
    private String topic;

    @Transactional
    public Item gacha(User user) throws JsonProcessingException {
        user.deductCoinForGacha();
        Item item = Item.gacha();
        user.increaseScoreByItem(item, userItemReader.findAllByUser(user));

        userUpdater.update(user);
        userItemAppender.save(UserItem.of(user, item));

        LottoIssuanceEvent lottoIssuanceEvent = new LottoIssuanceEvent(user.getId(), item.getItemGrade());
        String payload = objectMapper.writeValueAsString(lottoIssuanceEvent);
        outboxProcessor.save(Outbox.create(topic, payload));
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
