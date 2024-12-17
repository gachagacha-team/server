package gachagacha.gachagacha.item.repository;

import gachagacha.gachagacha.item.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    @Query("select ui from UserItem ui " +
            "where ui.user.nickname = :nickname")
    List<UserItem> findByUserNickname(@Param(value = "nickname") String nickname);
}
