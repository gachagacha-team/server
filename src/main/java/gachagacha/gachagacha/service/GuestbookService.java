package gachagacha.gachagacha.service;

import gachagacha.gachagacha.domain.Guestbook;
import gachagacha.gachagacha.domain.Minihome;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.implementation.guestbook.GuestbookAppender;
import gachagacha.gachagacha.implementation.guestbook.GuestbookReader;
import gachagacha.gachagacha.implementation.guestbook.GuestbookRemover;
import gachagacha.gachagacha.implementation.guestbook.GuestbookUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuestbookService {

    private final GuestbookReader guestbookReader;
    private final GuestbookAppender guestbookAppender;
    private final GuestbookUpdater guestbookUpdater;
    private final GuestbookRemover guestbookRemover;

    public Guestbook readGuestbook(long guestbookId) {
        return guestbookReader.read(guestbookId);
    }

    public Page<Guestbook> readGuestbooksByMinihome(Minihome minihome, Pageable pageable) {
        return guestbookReader.findByMinihome(minihome, pageable);
    }

    public long addGuestbook(Minihome minihome, User author, String content) {
        return guestbookAppender.save(minihome, author, content);
    }

    @Transactional
    public long editGuestbook(Guestbook guestbook, User user, String content) {
        validateAccessAuthorization(user, guestbook);
        return guestbookUpdater.update(guestbook, content);
    }

    public void deleteGuestbook(long guestbookId, User user) {
        Guestbook guestbook = guestbookReader.read(guestbookId);
        validateAccessAuthorization(user, guestbook);
        guestbookRemover.delete(guestbook);
    }

    private void validateAccessAuthorization(User user, Guestbook guestbook) {
        if (guestbook.getUserId() != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
