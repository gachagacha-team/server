package gachagacha.gachaapi.service;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.guestbook.Guestbook;
import gachagacha.domain.guestbook.GuestbookRepository;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuestbookService {

    private final GuestbookRepository guestbookRepository;

    public Guestbook readGuestbook(long guestbookId) {
        return guestbookRepository.findById(guestbookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
    }

    public Page<Guestbook> readGuestbooksByMinihome(Minihome minihome, Pageable pageable) {
        return guestbookRepository.findByMinihome(minihome, pageable);
    }

    public Long addGuestbook(Minihome minihome, User author, String content) {
        return guestbookRepository.save(Guestbook.createInitialGuestbook(minihome.getId(), author.getId(), content));
    }

    @Transactional
    public long editGuestbook(Guestbook guestbook, User user, String content) {
        validateAccessAuthorization(user, guestbook);
        guestbook.updateContent(content);
        return guestbookRepository.update(guestbook);
    }

    public void deleteGuestbook(long guestbookId, User user) {
        Guestbook guestbook = guestbookRepository.findById(guestbookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        validateAccessAuthorization(user, guestbook);
        guestbookRepository.delete(guestbook);
    }

    private void validateAccessAuthorization(User user, Guestbook guestbook) {
        if (guestbook.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
