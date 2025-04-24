package gachagacha.domain.guestbook;

import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestbookRepository {

    Page<Guestbook> findByMinihomeId(long miniHomeId, Pageable pageable);

    List<Guestbook> findByMinihomeId(long miniHomeId);

    void softDeleteByMinihome(Minihome minihome);

    void softDeleteByAuthor(User user);

    Long save(Guestbook guestbook);

    Optional<Guestbook> findById(Long id);

    void delete(Guestbook guestbook);

    Page<Guestbook> findByMinihome(Minihome minihome, Pageable pageable);

    Long update(Guestbook guestbook);
}
