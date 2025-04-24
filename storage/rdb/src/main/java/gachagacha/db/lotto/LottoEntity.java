package gachagacha.db.lotto;

import gachagacha.db.BaseEntity;
import gachagacha.domain.item.ItemGrade;
import gachagacha.domain.lotto.Lotto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "lotto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "item_grade"})})
public class LottoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lotto_id")
    private Long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemGrade itemGrade;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private boolean won;

    @Column(nullable = false)
    private int rewardCoin;

    public static LottoEntity fromLotto(Lotto lotto) {
        return new LottoEntity(lotto.getId(), lotto.getUserId(), lotto.getItemGrade(),
                lotto.isUsed(), lotto.isWon(), lotto.getRewardCoin());
    }

    public Lotto toLotto() {
        return new Lotto(id, userId, itemGrade, used, won, rewardCoin);
    }

    public void updateFromLotto(Lotto lotto) {
        this.userId = lotto.getUserId();
        this.used = lotto.isUsed();
        this.won = lotto.isWon();
        this.rewardCoin = lotto.getRewardCoin();
    }
}
