package gachagacha.gachagacha.domain;

import gachagacha.gachagacha.entity.UserItemEntity;
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

    public void addScoreForNewItem(Item item, List<UserItemEntity> userItemEntities) {
        if (userItemEntities.stream().noneMatch(userItem -> userItem.getItem() == item)) {
            score += item.getItemGrade().getScore();
        }
    }

    public void decrease(int score) {
        this.score -= score;
    }

    public void increase(int score) {
        this.score += score;
    }
}
