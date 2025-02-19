package gachagacha.gachagacha.minihome.repository;

import gachagacha.gachagacha.minihome.entity.Guestbook;
import gachagacha.gachagacha.minihome.entity.Minihome;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    Page<Guestbook> findByMinihome(Minihome miniHome, Pageable pageable);

    List<Guestbook> findByMinihome(Minihome miniHome);

    List<Guestbook> findByUser(User user);
}
