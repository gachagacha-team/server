package gachagacha.gachagacha.item.repository;

import gachagacha.gachagacha.item.entity.Background;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {

    @Query("select b from Background b " +
            "where b.user.nickname = :nickname")
    List<Background> findByUserNickname(@Param(value = "nickname") String nickname);
}
