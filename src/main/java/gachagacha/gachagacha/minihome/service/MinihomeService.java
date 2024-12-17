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
    private final JwtUtils jwtUtils;

    public MinihomeResponse visitMinihome(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Minihome miniHome = user.getMiniHome();
        miniHome.visit();
        return new MinihomeResponse(nickname, user.getCoin(), miniHome.getTotalVisitorCnt(), miniHome.getLayout());
    }

    public Slice<GuestbookResponse> getGuestbook(String nickname, Pageable pageable) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Minihome miniHome = user.getMiniHome();
       return guestbookRepository.findByMinihome(miniHome, pageable)
               .map(guestbook -> new GuestbookResponse(guestbook.getUser().getNickname(), guestbook.getContent()));
    }

    public AddGuestbookResponse addGuestbook(String nickname, AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        User minihomeUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        String guestbookUserNickname = jwtUtils.getNicknameFromHeader(request);
        User guestbookUser = userRepository.findByNickname(guestbookUserNickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Guestbook guestbook = Guestbook.create(guestbookUser, addGuestbookRequest.getContent());
        minihomeUser.getMiniHome().addGuestbook(guestbook);
        guestbookRepository.save(guestbook);

        return new AddGuestbookResponse(guestbook.getId(), guestbookUserNickname, guestbook.getContent());
    }

    public Slice<ExploreMinihomeResponse> explore(Pageable pageable) {
        return minihomeRepository.findAllBy(pageable)
                .map(minihome -> {
                    User user = userRepository.findByMinihome(minihome)
                            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
                    return new ExploreMinihomeResponse(user.getNickname(), minihome.getTotalVisitorCnt(), "/minihome/" + user.getNickname());
                });
    }
}
