package gachagacha.domain.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MinihomeMeta {

    private Long id;
    private Long minihomeId;
    private Long likeCount;

    public static MinihomeMeta createInitialMinihomeMeta(Long minihomeId) {
        return new MinihomeMeta(null, minihomeId, 0l);
    }
}
