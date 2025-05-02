package gachagacha.gachaapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.item.UserItemRepository;
import gachagacha.domain.lotto.LottoIssuanceEvent;
import gachagacha.domain.outbox.Outbox;
import gachagacha.domain.decoration.DecorationRepository;
import gachagacha.domain.outbox.OutboxRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final DecorationRepository decorationRepository;

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.stream.lotto-issuance-requests}")
    private String topic;

    @Transactional
    public Item gacha(User user) throws JsonProcessingException {
        user.deductCoinForGacha();
        Item item = Item.gacha();
        user.increaseScoreByItem(item, userItemRepository.findByUser(user));

        userRepository.update(user);
        userItemRepository.save(new UserItem(null, item, user.getId()));

        LottoIssuanceEvent lottoIssuanceEvent = new LottoIssuanceEvent(user.getId(), item.getItemGrade());
        String payload = objectMapper.writeValueAsString(lottoIssuanceEvent);
        outboxRepository.save(new Outbox(null, topic, payload));
        return item;
    }

    public UserItem readById(long userItemId) {
        return userItemRepository.findById(userItemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
    }

    public List<UserItem> readUserItemsByItem(User user, Item item) {
        return userItemRepository.findByUserAndItem(user, item);
    }

    public List<UserItem> readAllUserItems(User user) {
        return userItemRepository.findByUser(user);
    }

    public List<UserItem> readUserItemsExcludeDecorationItem(User user) {
        List<UserItem> userItems = userItemRepository.findByUser(user);
        Decoration decoration = decorationRepository.read(user);

        if (decoration.getDecorationItems() == null) {
            return userItems;
        }

        Set<Long> decorationUserItemIds = decoration.getDecorationItems().stream()
                .map(decorationItem -> decorationItem.getUserItemId())
                .collect(Collectors.toSet());

        return userItems.stream()
                .filter(userItem -> !decorationUserItemIds.contains(userItem.getId()))
                .toList();
    }
}
