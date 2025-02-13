package gachagacha.gachagacha.minihome.repository;

import gachagacha.gachagacha.minihome.entity.Guestbook;
import gachagacha.gachagacha.minihome.entity.Minihome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    Page<Guestbook> findByMinihome(Minihome miniHome, Pageable pageable);
}
