package gachagacha.gachagacha.item.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public static Item create(ItemType itemType) {
        Item item = new Item();
        item.itemType = itemType;
        return item;
    }
}
