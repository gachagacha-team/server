package gachagacha.gachagacha.minihome.repository;

import gachagacha.gachagacha.minihome.entity.Guestbook;
import gachagacha.gachagacha.minihome.entity.Minihome;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    Slice<Guestbook> findByMinihome(Minihome miniHome, Pageable pageable);
}
