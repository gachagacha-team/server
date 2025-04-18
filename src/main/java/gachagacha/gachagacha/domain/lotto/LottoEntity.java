package gachagacha.gachagacha.domain.lotto;

import gachagacha.gachagacha.domain.BaseEntity;
import gachagacha.gachagacha.domain.item.ItemGrade;
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
