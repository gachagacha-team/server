package gachagacha.gachagacha.implementation.guestbook;

import gachagacha.gachagacha.repository.GuestbookRepository;
import gachagacha.gachagacha.domain.Guestbook;
import gachagacha.gachagacha.domain.Minihome;
import gachagacha.gachagacha.domain.User;
import gachagacha.gachagacha.entity.GuestbookEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuestbookAppender {

    private final GuestbookRepository guestbookRepository;

    public long save(Minihome minihome, User author, String content) {
        Guestbook guestbook = Guestbook.of(minihome.getId(), author.getId(), content);
        GuestbookEntity saveGuestbookEntity = guestbookRepository.save(guestbook.toGuestbookEntity());
        return saveGuestbookEntity.getId();
    }
}
