package gachagacha.domain.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    private Long id;
    private Long followeeId;
    private Long followerId;
}
