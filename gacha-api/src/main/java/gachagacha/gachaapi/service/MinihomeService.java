package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import gachagacha.storageredis.MinihomeRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinihomeService {

    private final MinihomeRepository minihomeRepository;
    private final UserRepository userRepository;

    private final MinihomeRedisRepository minihomeRedisRepository;

    public Minihome readMinihome(User minihomeUser) {
        return minihomeRepository.findByUser(minihomeUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
    }

    @Async
    public void visitMinihome(Long minihomeId) {
        minihomeRedisRepository.increaseVisitorCount(minihomeId);
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
