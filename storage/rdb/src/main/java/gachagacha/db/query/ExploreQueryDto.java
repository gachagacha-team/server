package gachagacha.db.query;

import gachagacha.domain.user.Profile;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExploreQueryDto {

    private long minihomeId;
    private long userId;
    private String nickname;
    private Profile profile;
    private int totalVisitorCnt;
    private long likeCount;
    private int score;
    private LocalDateTime createdAt;

    public ExploreQueryDto(long minihomeId, long userId, String nickname, Profile profile, int totalVisitorCnt, long likeCount, int score, LocalDateTime createdAt) {
        this.minihomeId = minihomeId;
        this.userId = userId;
        this.nickname = nickname;
        this.profile = profile;
        this.totalVisitorCnt = totalVisitorCnt;
        this.likeCount = likeCount;
        this.score = score;
        this.createdAt = createdAt;
    }
}
