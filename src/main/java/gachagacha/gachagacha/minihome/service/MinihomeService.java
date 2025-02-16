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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MinihomeService {

    private final UserRepository userRepository;
    private final MinihomeRepository minihomeRepository;
    private final GuestbookRepository guestbookRepository;
    private final FollowRepository followRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public MinihomeResponse readMinihome(String nickname, HttpServletRequest request) {
        String currentUserNickname = jwtUtils.getUserNicknameFromHeader(request);

        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Minihome miniHome = minihomeUser.getMinihome();
        miniHome.visit();

        int followersCnt = followRepository.findByFollowee(minihomeUser).size();
        int followingsCnt = followRepository.findByFollower(minihomeUser).size();

        return new MinihomeResponse(minihomeUser.getNickname().equals(currentUserNickname), minihomeUser.getNickname(), minihomeUser.getScore().getScore(), followersCnt, followingsCnt, miniHome.getTotalVisitorCnt(), "/image/profile/" + minihomeUser.getProfileImage().getStoreFileName(), miniHome.getLayout());
    }

    @Transactional(readOnly = true)
    public Page<GuestbookResponse> readGuestbooks(String nickname, Pageable pageable, HttpServletRequest request) {
        User currentUser = userRepository.findByNickname(jwtUtils.getUserNicknameFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Minihome miniHome = minihomeUser.getMinihome();
       return guestbookRepository.findByMinihome(miniHome, pageable)
               .map(guestbook -> new GuestbookResponse(guestbook.getId(), guestbook.getUser().getNickname(), guestbook.getContent(),
                       guestbook.getCreatedAt(), guestbook.getUser().getNickname().equals(currentUser.getNickname())));
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
    public GuestbookResponse editGuestbook(long guestbookId, EditGuestbookRequest editGuestbookRequest) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbook.edit(editGuestbookRequest.getContent());
        return new GuestbookResponse(guestbook.getId(), guestbook.getUser().getNickname(), guestbook.getContent(), guestbook.getCreatedAt(), true);
    }

    @Transactional
    public void deleteGuestbook(long guestbookId) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbookRepository.delete(guestbook);
    }

    @Transactional(readOnly = true)
    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable)
                .map(minihome -> {
                    User user = userRepository.findByMinihome(minihome)
                            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
                    return new ExploreMinihomeResponse(user.getNickname(), minihome.getTotalVisitorCnt(), "/image/profile/" + user.getProfileImage().getStoreFileName());
                });
    }
}
