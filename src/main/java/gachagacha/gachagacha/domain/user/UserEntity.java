package gachagacha.gachagacha.domain.user;

import gachagacha.gachagacha.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "socialType", "loginId" }) })
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column
    private Long loginId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Embedded
    private Coin coin;

    @Column(nullable = false)
    @Embedded
    private Score score;

    @Enumerated(value = EnumType.STRING)
    private Profile profile;

    private List<Background> backgrounds = new ArrayList<>();

    public User toUser() {
        return new User(id, socialType, loginId, nickname, coin, score,
                profile,
                backgrounds);
    }

    public void updateFromUser(User user) {
        this.nickname = user.getNickname();
        this.coin = user.getCoin();
        this.score = user.getScore();
        this.profile = user.getProfile();
        this.backgrounds = user.getBackgrounds();
    }
}
