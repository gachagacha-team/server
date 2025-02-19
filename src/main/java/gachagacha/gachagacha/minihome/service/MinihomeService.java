package gachagacha.gachagacha.minihome.service;

import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.minihome.dto.*;
import gachagacha.gachagacha.minihome.entity.Guestbook;
import gachagacha.gachagacha.minihome.repository.GuestbookRepository;
import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.minihome.repository.MinihomeRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.FollowRepository;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MinihomeService {

    @Value("${image.api.endpoints.profile}")
    private String profileImageApiEndpoint;

    private final UserRepository userRepository;
    private final MinihomeRepository minihomeRepository;
    private final GuestbookRepository guestbookRepository;
    private final FollowRepository followRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public MinihomeResponse readMinihome(String nickname, HttpServletRequest request) {
        User currentUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Minihome miniHome = minihomeUser.getMinihome();
        miniHome.visit();

        int followersCnt = followRepository.findByFollowee(minihomeUser).size();
        int followingsCnt = followRepository.findByFollower(minihomeUser).size();

        boolean isFollowing = followRepository.findByFollowerAndFollowee(currentUser, minihomeUser).isPresent();

        return new MinihomeResponse(minihomeUser.getNickname().equals(currentUser.getNickname()),
                minihomeUser.getNickname(), minihomeUser.getScore().getScore(),
                followersCnt, followingsCnt, miniHome.getTotalVisitorCnt(),
                profileImageApiEndpoint + minihomeUser.getProfileImage().getStoreFileName(),
                miniHome.getLayout(), isFollowing);
    }

    @Transactional(readOnly = true)
    public Page<GuestbookResponse> readGuestbooks(String nickname, Pageable pageable, HttpServletRequest request) {
        User currentUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Minihome miniHome = minihomeUser.getMinihome();

        return guestbookRepository.findByMinihome(miniHome, pageable)
                .map(guestbook -> {
                    if (guestbook.getUser() == null) {
                        return new GuestbookResponse(guestbook.getId(), "undefined", guestbook.getContent(),
                                guestbook.getCreatedAt(), false);
                    }
                    return new GuestbookResponse(guestbook.getId(), guestbook.getUser().getNickname(), guestbook.getContent(),
                            guestbook.getCreatedAt(), guestbook.getUser().getNickname().equals(currentUser.getNickname()));
                });
    }

    @Transactional
    public GuestbookResponse addGuestbook(String nickname, AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        String guestbookUserNickname = jwtUtils.getUserNicknameFromHeader(request);
        User guestbookUser = userRepository.findByNickname(guestbookUserNickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Guestbook guestbook = Guestbook.create(guestbookUser, addGuestbookRequest.getContent());
        minihomeUser.getMinihome().addGuestbook(guestbook);
        guestbookRepository.save(guestbook);

        return new GuestbookResponse(guestbook.getId(), guestbookUser.getNickname(), guestbook.getContent(), guestbook.getCreatedAt(), true);
    }

    @Transactional
    public GuestbookResponse editGuestbook(long guestbookId, EditGuestbookRequest editGuestbookRequest, HttpServletRequest request) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        User guestbookUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if (!guestbook.getUser().getNickname().equals(guestbookUser.getNickname())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        guestbook.edit(editGuestbookRequest.getContent());
        return new GuestbookResponse(guestbook.getId(), guestbook.getUser().getNickname(), guestbook.getContent(), guestbook.getCreatedAt(), true);
    }

    @Transactional
    public void deleteGuestbook(long guestbookId, HttpServletRequest request) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        User guestbookUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if (!guestbook.getUser().getNickname().equals(guestbookUser.getNickname())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        guestbookRepository.delete(guestbook);
    }

    @Transactional(readOnly = true)
    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable)
                .map(minihome -> {
                    User user = userRepository.findByMinihome(minihome)
                            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
                    return new ExploreMinihomeResponse(user.getNickname(), minihome.getTotalVisitorCnt(),
                            profileImageApiEndpoint + user.getProfileImage().getStoreFileName());
                });
    }

    @Transactional(readOnly = true)
    public Slice<ExploreMinihomeResponse> exploreByScore(Pageable pageable) {
        return userRepository.findAllBy(pageable)
                .map(user -> new ExploreMinihomeResponse(user.getNickname(), user.getMinihome().getTotalVisitorCnt(),
                        profileImageApiEndpoint + user.getProfileImage().getStoreFileName()
                ));
    }
}
