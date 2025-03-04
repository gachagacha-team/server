package gachagacha.gachagacha.domain.guestbook;

import gachagacha.gachagacha.domain.item.entity.GuestbookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestbookRepository extends JpaRepository<GuestbookEntity, Long> {

    Page<GuestbookEntity> findByMinihomeId(long miniHomeId, Pageable pageable);
    List<GuestbookEntity> findByMinihomeId(long miniHomeId);

    @Modifying
    @Query("update GuestbookEntity g " +
            "set g.minihomeId = -1 " +
            "where g.minihomeId = :minihomeId")
    void softDeleteByMinihome(@Param("minihomeId") long minihomeId);

    @Modifying
    @Query("update GuestbookEntity g " +
            "set g.userId = -1 " +
            "where g.userId = :userId")
    void softDeleteByAuthor(@Param("userId") long userId);
}
