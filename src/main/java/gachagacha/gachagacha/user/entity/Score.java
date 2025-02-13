package gachagacha.gachagacha.user.entity;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.UserItem;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.List;

@Embeddable
@Getter
public class Score {

    private int score;
    private static final int INITIAL_SCORE = 0;

    public Score() {
        this.score = INITIAL_SCORE;
    }

    public void addScoreForNewItem(Item item, List<UserItem> userItems) {
        if (userItems.stream().noneMatch(userItem -> userItem.getItem() == item)) {
            score += item.getItemGrade().getScore();
        }
    }

    public void decreaseScoreForSaleItem(List<UserItem> userItems, Item removedItem) {
        long itemCount = userItems.stream()
                .filter(userItem -> userItem.getItem() == removedItem)
                .count();
        if (itemCount == 0) {
            score -= removedItem.getItemGrade().getScore();
        }
    }
}
