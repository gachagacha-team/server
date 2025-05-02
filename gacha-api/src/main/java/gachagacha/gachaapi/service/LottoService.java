package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.lotto.Lotto;
import gachagacha.domain.lotto.LottoRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final LottoRepository lottoRepository;
    private final UserRepository userRepository;

    public List<Lotto> findUnusedLottos(User user) {
        return lottoRepository.findByUserAndUsed(user, false);
    }

    @Transactional
    public Lotto useLotto(long lottoId, User user) {
        Lotto lotto = lottoRepository.findById(lottoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOTTO));
        if (lotto.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (lotto.isUsed()) {
            throw new BusinessException(ErrorCode.ALREADY_USED_LOTTO);
        }

        lotto.use();
        user.addCoin(lotto.getRewardCoin());
        lottoRepository.update(lotto);
        userRepository.update(user);
        return lotto;
    }
}
