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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MinihomeService {

    private final UserRepository userRepository;
    private final MinihomeRepository minihomeRepository;
    private final GuestbookRepository guestbookRepository;
    private final FollowRepository followRepository;
    private final JwtUtils jwtUtils;

    public MinihomeResponse getMinihome(String nickname, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);

        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Minihome miniHome = minihomeUser.getMiniHome();
        miniHome.visit();

        int followersCnt = followRepository.findByFollowee(minihomeUser).getSize();
        int followingsCnt = followRepository.findByFollower(minihomeUser).getSize();

        return new MinihomeResponse(minihomeUser.getId() == userId, minihomeUser.getId(), minihomeUser.getNickname(), followersCnt, followingsCnt, miniHome.getTotalVisitorCnt(), minihomeUser.getProfileImageUrl(), miniHome.getLayout());
    }

    public Slice<GuestbookResponse> getGuestbooks(String nickname, Pageable pageable, HttpServletRequest request) {
        User viewUser = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Minihome miniHome = minihomeUser.getMiniHome();
       return guestbookRepository.findByMinihome(miniHome, pageable)
               .map(guestbook -> new GuestbookResponse(guestbook.getId(), guestbook.getUser().getNickname(), guestbook.getContent(),
                       guestbook.getCreatedAt(), guestbook.getUser().getNickname().equals(viewUser.getNickname())));
    }

    public GuestbookResponse addGuestbook(String nickname, AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        long guestbookUserId = jwtUtils.getUserIdFromHeader(request);
        User guestbookUser = userRepository.findById(guestbookUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Guestbook guestbook = Guestbook.create(guestbookUser, addGuestbookRequest.getContent());
        minihomeUser.getMiniHome().addGuestbook(guestbook);
        guestbookRepository.save(guestbook);

        return new GuestbookResponse(guestbook.getId(), guestbookUser.getNickname(), guestbook.getContent(), guestbook.getCreatedAt(), true);
    }

    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable)
                .map(minihome -> {
                    User user = userRepository.findByMinihome(minihome)
                            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
                    return new ExploreMinihomeResponse(user.getNickname(), minihome.getTotalVisitorCnt(), user.getProfileImageUrl());
                });
    }

    public GuestbookResponse editGuestbook(long guestbookId, EditGuestbookRequest editGuestbookRequest) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbook.edit(editGuestbookRequest.getContent());
        return new GuestbookResponse(guestbook.getId(), guestbook.getUser().getNickname(), guestbook.getContent(), guestbook.getCreatedAt(), true);
    }

    public void deleteGuestbook(long guestbookId) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbookRepository.delete(guestbook);
    }
}
