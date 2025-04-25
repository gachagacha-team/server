package gachagacha.db.item;

import gachagacha.db.BaseEntity;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.UserItem;
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

    public static UserItemEntity fromUserItem(UserItem userItem) {
        return new UserItemEntity(userItem.getId(), userItem.getItem(), userItem.getUserId());
    }

    public UserItem toUserItem() {
        return new UserItem(id, item, userId);
    }
}
