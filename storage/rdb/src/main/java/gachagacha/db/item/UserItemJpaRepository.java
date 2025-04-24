package gachagacha.db.item;

import gachagacha.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserItemJpaRepository extends JpaRepository<UserItemEntity, Long> {

    List<UserItemEntity> findByUserId(long userId);

    Page<UserItemEntity> findByUserId(long userId, Pageable pageable);

    List<UserItemEntity> findByUserIdAndItem(long userId, Item item);

    void deleteAllByUserId(long userId);
}
