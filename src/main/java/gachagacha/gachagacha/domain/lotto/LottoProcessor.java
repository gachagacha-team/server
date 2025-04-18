package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LottoProcessor {

    private final LottoRepository lottoRepository;

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
