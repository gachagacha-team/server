package gachagacha.db.guestbook;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.guestbook.Guestbook;
import gachagacha.domain.guestbook.GuestbookRepository;
import gachagacha.domain.minihome.Minihome;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuestbookEntityRepository implements GuestbookRepository {

    private final GuestbookJpaRepository guestbookJpaRepository;

    @Override
    public Page<Guestbook> findByMinihomeId(long miniHomeId, Pageable pageable) {
        return guestbookJpaRepository.findByMinihomeId(miniHomeId, pageable)
                .map(guestbookEntity -> guestbookEntity.toGuestbook());
    }

    @Override
    public List<Guestbook> findByMinihomeId(long miniHomeId) {
        return guestbookJpaRepository.findByMinihomeId(miniHomeId)
                .stream().map(guestbookEntity -> guestbookEntity.toGuestbook())
                .toList();
    }

    @Override
    public void softDeleteByMinihome(Minihome minihome) {
        guestbookJpaRepository.softDeleteByMinihome(minihome.getId());
    }

    @Override
    public void softDeleteByAuthor(User user) {
        guestbookJpaRepository.softDeleteByAuthor(user.getId());
    }

    @Override
    public Long save(Guestbook guestbook) {
        GuestbookEntity guestbookEntity = guestbookJpaRepository.save(GuestbookEntity.fromGuestbook(guestbook));
        return guestbookEntity.getId();
    }

    @Override
    public Optional<Guestbook> findById(Long id) {
        return guestbookJpaRepository.findById(id)
                .map(guestbookEntity -> guestbookEntity.toGuestbook());
    }

    @Override
    public void delete(Guestbook guestbook) {
        guestbookJpaRepository.delete(GuestbookEntity.fromGuestbook(guestbook));
    }

    @Override
    public Page<Guestbook> findByMinihome(Minihome minihome, Pageable pageable) {
        return guestbookJpaRepository.findByMinihomeId(minihome.getId(), pageable)
                .map(guestbookEntity -> guestbookEntity.toGuestbook());
    }

    @Override
    public Long update(Guestbook guestbook) {
        GuestbookEntity guestbookEntity = guestbookJpaRepository.findById(guestbook.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_GUESTBOOK));
        guestbookEntity.updateFromGuestbook(guestbook);
        return guestbookEntity.getId();
    }
}
