package gachagacha.db.lotto;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.lotto.Lotto;
import gachagacha.domain.lotto.LottoRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LottoEntityRepository implements LottoRepository {

    private final LottoJpaRepository lottoJpaRepository;

    @Override
    public Long save(Lotto lotto) {
        LottoEntity lottoEntity = lottoJpaRepository.save(LottoEntity.fromLotto(lotto));
        return lottoEntity.getId();
    }

    @Override
    public List<Lotto> findByUserAndUsed(User user, boolean used) {
        return lottoJpaRepository.findByUserIdAndUsed(user.getId(), used).stream()
                .map(lottoEntity -> lottoEntity.toLotto())
                .toList();
    }

    @Override
    public Optional<Lotto> findById(long lottoId) {
        return lottoJpaRepository.findById(lottoId)
                .map(lottoEntity -> lottoEntity.toLotto());
    }

    @Override
    public void update(Lotto lotto) {
        LottoEntity lottoEntity = lottoJpaRepository.findById(lotto.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOTTO));
        lottoEntity.updateFromLotto(lotto);
    }
}
