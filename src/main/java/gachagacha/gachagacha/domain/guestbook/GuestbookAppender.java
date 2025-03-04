package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.item.entity.GuestbookEntity;
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
