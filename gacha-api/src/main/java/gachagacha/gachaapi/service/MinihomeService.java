package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MinihomeService {

    private final MinihomeRepository minihomeRepository;
    private final UserRepository userRepository;

    public Minihome readMinihome(User user) {
        return minihomeRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
    }

    @Transactional
    public Minihome visitMinihome(User minihomeUser) {
        Minihome minihome = minihomeRepository.findByUser(minihomeUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
        minihome.visit();
        minihomeRepository.update(minihome);
        return minihome;
    }

    public Slice<Minihome> explore(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable);
    }

    public Slice<Minihome> exploreByScore(Pageable pageable) {
        return userRepository.findAllBy(pageable)
                .map(user -> minihomeRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME)));
    }
}
