package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_item_id")
    private long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public static UserItem create(Item item) {
        UserItem userItem = new UserItem();
        userItem.item = item;
        return userItem;
    }
}
