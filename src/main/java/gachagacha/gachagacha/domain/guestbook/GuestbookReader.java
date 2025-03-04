package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestbookReader {

    private final GuestbookRepository guestbookRepository;


    public Guestbook read(long guestbookId) {
        return guestbookRepository.findById(guestbookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK))
                .toGuestbook();
    }

    public Page<Guestbook> findByMinihome(Minihome minihome, Pageable pageable) {
        return guestbookRepository.findByMinihomeId(minihome.getId(), pageable)
                .map(guestbookEntity -> guestbookEntity.toGuestbook());
    }
}
