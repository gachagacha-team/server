package gachagacha.lotto.lotto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.ItemGrade;
import gachagacha.domain.item.UserItem;
import gachagacha.domain.item.UserItemRepository;
import gachagacha.domain.lotto.Lotto;
import gachagacha.domain.lotto.LottoRepository;
import gachagacha.domain.outbox.Outbox;
import gachagacha.domain.outbox.OutboxRepository;
import gachagacha.lotto.dto.IssuedLotto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LottoIssuanceService {

    private final LottoRepository lottoRepository;
    private final UserItemRepository userItemRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public boolean checkLottoIssuanceCondition(Long userId, ItemGrade itemGrade) {
        List<UserItem> userItems = userItemRepository.findByUserId(userId);
        if (userItems == null) {
            return false;
        }

        Map<Item, Long> userItemsMap = userItems.stream()
                .filter(userItem -> userItem.getItem().getItemGrade() == itemGrade)
                .collect(Collectors.groupingBy(
                        userItem -> userItem.getItem(),
                        Collectors.counting()
                ));

        boolean hasAllItemsOfGrade = Item.getItemsByGrade(itemGrade)
                .stream()
                .allMatch(item -> userItemsMap.getOrDefault(item, 0L) > 0);
        return hasAllItemsOfGrade;
    }

    @Transactional
    public void issueAndSaveLotto(long userId, ItemGrade itemGrade, String topic) throws JsonProcessingException {
        boolean won = won();
        int rewardCoin = 0;
        if (won) {
            rewardCoin = getRandomCoin();
        }
        Long lottoId = lottoRepository.save(Lotto.createInitialLotto(userId, itemGrade, won, rewardCoin));
        String payload = objectMapper.writeValueAsString(new IssuedLotto(userId, lottoId));
        outboxRepository.save(new Outbox(null, topic, payload));
    }

    private boolean won() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private int getRandomCoin() {
        Random random = new Random();
        int min = 3000;
        int max = 10000;
        return ((random.nextInt((max - min) / 1000 + 1) + min / 1000) * 1000);
    }
}
