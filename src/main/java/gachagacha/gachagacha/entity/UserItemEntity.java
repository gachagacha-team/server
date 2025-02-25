package gachagacha.gachagacha.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.domain.Item;
import gachagacha.gachagacha.domain.UserItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_item")
@NoArgsConstructor
@AllArgsConstructor
public class UserItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_item_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Item item;

    @Column(nullable = false)
    private Long userId;

    public UserItem toUserItem() {
        return new UserItem(id, item, userId);
    }
}
