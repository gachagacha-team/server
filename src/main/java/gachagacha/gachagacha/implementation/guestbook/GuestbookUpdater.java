package gachagacha.gachagacha.implementation.guestbook;

import gachagacha.gachagacha.repository.GuestbookRepository;
import gachagacha.gachagacha.domain.Guestbook;
import gachagacha.gachagacha.entity.GuestbookEntity;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GuestbookUpdater {

    private final GuestbookRepository guestbookRepository;

    @Transactional
    public long update(Guestbook guestbook, String content) {
        guestbook.updateContent(content);
        GuestbookEntity guestbookEntity = guestbookRepository.findById(guestbook.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbookEntity.updateFromGuestbook(guestbook);
        return guestbookEntity.getId();
    }
}
