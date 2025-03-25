package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.UserItemRepository;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
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
            lottoMessagePublisher.publishLottoIssuanceEvent(user.getId(), addedItem.getItemGrade());
        }
    }

    public Lotto save(Lotto lotto) {
        LottoEntity savedLottoEntity = lottoRepository.save(lotto.toLottoEntity());
        return savedLottoEntity.toLotto();
    }

    public List<Lotto> findUnusedLottos(User user) { // 사용하지 않은것만, 생성기준 최신기준으로
        return lottoRepository.findByUserIdAndUsed(user.getId(), false)
                .stream()
                .map(LottoEntity::toLotto)
                .toList();
    }

    public Lotto findById(long lottoId) {
        return lottoRepository.findById(lottoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOTTO))
                .toLotto();
    }

    public void update(Lotto lotto) {
        LottoEntity lottoEntity = lottoRepository.findById(lotto.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOTTO));
        lottoEntity.updateFromLotto(lotto);
    }
}
