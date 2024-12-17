package gachagacha.gachagacha.item.repository;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where i.user.nickname = :nickname")
    List<Item> findByUserNickname(@Param(value = "nickname") String nickname);
}
