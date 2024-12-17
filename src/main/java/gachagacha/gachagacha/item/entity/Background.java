package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Background extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "background_id")
    private long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BackgroundType backgroundType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public static Background create(BackgroundType backgroundType) {
        Background background = new Background();
        background.backgroundType = backgroundType;
        return background;
    }
}
