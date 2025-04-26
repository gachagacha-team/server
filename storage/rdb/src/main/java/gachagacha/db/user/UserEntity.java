package gachagacha.db.user;

import gachagacha.db.BaseEntity;
import gachagacha.domain.user.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Long loginId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private int coin;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Profile profile;

    public User toUser() {
        return new User(id, socialType, loginId, nickname, coin, score, profile);
    }

    public static UserEntity fromUser(User user) {
        return new UserEntity(
                user.getId(), user.getSocialType(), user.getLoginId(),
                user.getNickname(), user.getCoin().getCoin(), user.getScore().getScore(), user.getProfile()
        );
    }

    public void updateFromUser(User user) {
        this.nickname = user.getNickname();
        this.coin = user.getCoin().getCoin();
        this.score = user.getScore().getScore();
        this.profile = user.getProfile();
    }
}
