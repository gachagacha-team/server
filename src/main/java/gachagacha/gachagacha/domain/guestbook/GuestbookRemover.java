package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.support.exception.ErrorCode;
import gachagacha.gachagacha.support.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestbookRemover {

    private final GuestbookRepository guestbookRepository;

    public void delete(Guestbook guestbook) {
        GuestbookEntity guestbookEntity = guestbookRepository.findById(guestbook.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbookRepository.delete(guestbookEntity);
    }

    public void softDeleteByMinihome(Minihome minihome) {
        guestbookRepository.softDeleteByMinihome(minihome.getId());
    }

    public void softDeleteByAuthor(User user) {
        guestbookRepository.softDeleteByAuthor(user.getId());
    }
}
