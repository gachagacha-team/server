package gachagacha.gachagacha.domain.lotto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LottoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lotto_id")
    private Long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private boolean won;

    @Column(nullable = false)
    private int rewardCoin;

    public Lotto toLotto() {
        return new Lotto(id, userId, used, won, rewardCoin);
    }
}
