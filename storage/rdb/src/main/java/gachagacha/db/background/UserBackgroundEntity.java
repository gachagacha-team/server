package gachagacha.db.background;

import gachagacha.db.BaseEntity;
import gachagacha.domain.user.Background;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_background")
@AllArgsConstructor
@NoArgsConstructor
public class UserBackgroundEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_background_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Background background;

    @Column(nullable = false)
    private Long userId;
}
