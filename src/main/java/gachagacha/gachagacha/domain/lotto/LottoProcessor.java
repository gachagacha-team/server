package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItemRepository;
import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LottoProcessor {

    private final LottoRepository lottoRepository;
    private final UserItemRepository userItemRepository;
    private final LottoMessagePublisher lottoMessagePublisher;

    @Async
    public void checkAndPublishLotteryEvent(User user, Item addedItem) {
        Map<Item, Long> userItemsMap = userItemRepository.findByUserId(user.getId())
                .stream()
                .filter(userItem -> userItem.getItem().getItemGrade() == addedItem.getItemGrade())
                .collect(Collectors.groupingBy(
                        userItem -> userItem.getItem(),
                        Collectors.counting()
                ));

        boolean hasAllItemsOfGrade = Item.getItemsByGrade(addedItem.getItemGrade())
                .stream()
                .allMatch(item -> userItemsMap.getOrDefault(item, 0L) > 0);

        boolean addedItemIsOne = userItemsMap.getOrDefault(addedItem, 0L) == 1;

        if (hasAllItemsOfGrade && addedItemIsOne) {
            lottoMessagePublisher.publishLottoIssuanceEvent(user.getId());
        }
    }

    public Lotto save(Lotto lotto) {
        LottoEntity savedLottoEntity = lottoRepository.save(lotto.toLottoEntity());
        return savedLottoEntity.toLotto();
    }
}
