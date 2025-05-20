package gachagacha.gachaapi.service;

import gachagacha.db.query.ExploreQueryDto;
import gachagacha.db.query.ExploreQueryRepository;
import gachagacha.domain.like.Like;
import gachagacha.domain.like.LikeRepository;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.domain.user.User;
import gachagacha.domain.user.UserRepository;
import gachagacha.gachaapi.dto.response.ExploreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinihomeService {

    private final MinihomeRepository minihomeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ExploreQueryRepository exploreQueryRepository;

    public Minihome readMinihome(User minihomeUser) {
        return minihomeRepository.findByUserId(minihomeUser.getId());
    }

    @Async
    @Transactional
    public void visitMinihome(Long minihomeId) {
        minihomeRepository.increaseVisitorCount(minihomeId);
    }

    public ExploreResponse exploreByCreatedAt(Pageable pageable, LocalDateTime createdAt, Long minihomeId) {
        Slice<Long> minihomeIdsSlice = minihomeRepository.findMinihomeIdsOrderByCreatedAtDescAndMinihomeIdDesc(pageable, createdAt, minihomeId);
        List<ExploreQueryDto> exploreQueryDtos = exploreQueryRepository.findExploreDtosByMinihomeIds(minihomeIdsSlice.getContent());
        exploreQueryDtos.sort(
                Comparator.comparing(ExploreQueryDto::getCreatedAt).reversed()
                        .thenComparing(ExploreQueryDto::getMinihomeId, Comparator.reverseOrder())
        );
        return ExploreResponse.of(minihomeIdsSlice.isLast(), exploreQueryDtos);
    }

    public ExploreResponse exploreByTotalVisitorCnt(Pageable pageable, Integer totalVisitorCnt, Long minihomeId) {
        Slice<Long> minihomeIdsSlice = minihomeRepository.findMinihomeIdsOrderByTotalVisitorCntDescAndMinihomeIdDesc(pageable, totalVisitorCnt, minihomeId);
        List<ExploreQueryDto> exploreQueryDtos = exploreQueryRepository.findExploreDtosByMinihomeIds(minihomeIdsSlice.getContent());
        exploreQueryDtos.sort(
                Comparator.comparing(ExploreQueryDto::getTotalVisitorCnt).reversed()
                        .thenComparing(ExploreQueryDto::getMinihomeId, Comparator.reverseOrder())
        );
        return ExploreResponse.of(minihomeIdsSlice.isLast(), exploreQueryDtos);
    }

    public ExploreResponse exploreByScore(Pageable pageable, Integer score, Long userId) {
        Slice<Long> userIdsSlice = userRepository.findUserIdsOrderByScoreDescAndUserIdDesc(pageable, score, userId);
        List<ExploreQueryDto> exploreQueryDtos = exploreQueryRepository.findExploreDtosByUserIds(userIdsSlice.getContent());
        exploreQueryDtos.sort(
                Comparator.comparing(ExploreQueryDto::getScore).reversed()
                        .thenComparing(ExploreQueryDto::getUserId, Comparator.reverseOrder())
        );
        return ExploreResponse.of(userIdsSlice.isLast(), exploreQueryDtos);
    }

    public ExploreResponse exploreByLikeCount(Pageable pageable, long likeCount, long minihomeId) {
        Slice<Long> minihomeIdsSlice = minihomeRepository.findMinihomeIdsOrderByLikeCountDescAndMinihomeIdDesc(pageable, likeCount, minihomeId);

        List<ExploreQueryDto> exploreQueryDtos = exploreQueryRepository.findExploreDtosByMinihomeIds(minihomeIdsSlice.getContent());
        exploreQueryDtos.sort(
                Comparator.comparing(ExploreQueryDto::getLikeCount).reversed()
                        .thenComparing(ExploreQueryDto::getMinihomeId, Comparator.reverseOrder())
        );
        return ExploreResponse.of(minihomeIdsSlice.isLast(), exploreQueryDtos);
    }

    public void like(User minihomeUser, User currentUser) {
        Minihome minihome = minihomeRepository.findByUserId(minihomeUser.getId());
        Optional<Like> optionalLike = likeRepository.findByMinihomeIdAndAndUserId(minihome.getId(), currentUser.getId());
        if (optionalLike.isEmpty()) {
            likeRepository.save(new Like(null, minihome.getId(), currentUser.getId()));
            minihomeRepository.increaseLikeCount(minihome.getId());
        } else {
            likeRepository.delete(optionalLike.get());
            minihomeRepository.decreaseLikeCount(minihome.getId());
        }
    }

    public boolean isLike(User currentUser, Minihome minihome) {
        return likeRepository.findByMinihomeIdAndAndUserId(minihome.getId(), currentUser.getId()).isPresent();
    }
}
