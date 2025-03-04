package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestbookUpdater {

    private final GuestbookRepository guestbookRepository;

    public long update(Guestbook guestbook, String content) {
        guestbook.updateContent(content);
        GuestbookEntity guestbookEntity = guestbookRepository.findById(guestbook.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbookEntity.updateFromGuestbook(guestbook);
        return guestbookEntity.getId();
    }
}
