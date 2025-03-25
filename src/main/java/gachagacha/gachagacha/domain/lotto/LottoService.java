package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final LottoProcessor lottoProcessor;

    public List<Lotto> findUnusedLottos(User user) {
        return lottoProcessor.findUnusedLottos(user);
    }

    @Transactional
    public void useLotto(long lottoId, User user) {
        Lotto lotto = lottoProcessor.findById(lottoId);
        if (lotto.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (lotto.isUsed()) {
            throw new BusinessException(ErrorCode.ALREADY_USED_LOTTO);
        }

        lotto.use();
        lottoProcessor.update(lotto);
    }
}
