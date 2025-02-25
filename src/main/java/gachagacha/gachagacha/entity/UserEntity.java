package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.*;
import gachagacha.gachagacha.domain.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "socialType", "loginId" }) })
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

    @Column(nullable = false)
    @Embedded
    private ProfileImageEntity profileImageEntity;

    private List<Background> backgrounds = new ArrayList<>();

    public User toUser() {
        return new User(id, socialType, loginId, nickname, coin, score,
                new ProfileImage(profileImageEntity.getUploadFileName(), profileImageEntity.getStoreFileName()),
                backgrounds);
    }

    public void updateFromUser(User user) {
        this.nickname = user.getNickname();
        this.coin = user.getCoin();
        this.score = user.getScore();
        this.profileImageEntity = user.getProfileImage().toEntity();
        this.backgrounds = user.getBackgrounds();
    }
}
