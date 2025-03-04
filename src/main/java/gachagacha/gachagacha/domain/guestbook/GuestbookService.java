package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.domain.guestbook.Guestbook;
import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import gachagacha.gachagacha.domain.guestbook.GuestbookAppender;
import gachagacha.gachagacha.domain.guestbook.GuestbookReader;
import gachagacha.gachagacha.domain.guestbook.GuestbookRemover;
import gachagacha.gachagacha.domain.guestbook.GuestbookUpdater;
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
