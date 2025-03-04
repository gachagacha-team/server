package gachagacha.gachagacha.domain.item;

import gachagacha.gachagacha.domain.item.Item;
import gachagacha.gachagacha.domain.item.entity.UserItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserItemRepository extends JpaRepository<UserItemEntity, Long> {

    List<UserItemEntity> findByUserId(long userId);

    Page<UserItemEntity> findByUserId(long userId, Pageable pageable);

    List<UserItemEntity> findByUserIdAndItem(long userId, Item item);

    void deleteAllByUserId(long userId);
}
