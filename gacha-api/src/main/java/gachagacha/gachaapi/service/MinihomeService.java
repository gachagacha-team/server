package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.like.Like;
import gachagacha.domain.like.LikeRepository;
import gachagacha.domain.meta.MinihomeMetaRepository;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinihomeService {

    private final MinihomeRepository minihomeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final MinihomeMetaRepository minihomeMetaRepository;

    public Minihome readMinihome(User minihomeUser) {
        return minihomeRepository.findByUser(minihomeUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
    }

    @Async
    @Transactional
    public void visitMinihome(Long minihomeId) {
        minihomeRepository.increaseVisitorCount(minihomeId);
    }

    public Slice<Minihome> explore(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable);
    }

    public Slice<Minihome> exploreByScore(Pageable pageable) {
        return userRepository.findAllBy(pageable)
                .map(user -> minihomeRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME)));
    }

    public void like(User minihomeUser, User currentUser) {
        Minihome minihome = minihomeRepository.findByUser(minihomeUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MINIHOME));
        Optional<Like> optionalLike = likeRepository.findByMinihomeIdAndAndUserId(minihome.getId(), currentUser.getId());
        if (optionalLike.isEmpty()) {
            likeRepository.save(new Like(null, minihome.getId(), currentUser.getId()));
            minihomeMetaRepository.increaseLikeCount(minihome.getId());
        } else {
            likeRepository.delete(optionalLike.get());
            minihomeMetaRepository.decreaseLikeCount(minihome.getId());
        }
    }
}
