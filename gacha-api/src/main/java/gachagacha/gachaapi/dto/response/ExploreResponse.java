package gachagacha.gachaapi.dto.response;

import gachagacha.db.query.ExploreQueryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExploreResponse {

    private boolean last;
    private int size;
    private List<ExploreMinihomeDto> content;

    public static ExploreResponse of(boolean last, List<ExploreQueryDto> exploreQueryDtos) {
        List<ExploreMinihomeDto> content = exploreQueryDtos.stream()
                .map(exploreQueryDto -> ExploreMinihomeDto.of(exploreQueryDto))
                .toList();
        return new ExploreResponse(last, content.size(), content);
    }

    @Getter
    @AllArgsConstructor
    static public class ExploreMinihomeDto {

        private String nickname;
        private long profileId;
        private long minihomeId;
        private int totalVisitorCnt;
        private int score;
        private long likeCount;
        private LocalDateTime createdAt;

        public static ExploreMinihomeDto of(ExploreQueryDto exploreQueryDto) {
            return new ExploreMinihomeDto(exploreQueryDto.getNickname(),
                    exploreQueryDto.getProfile().getId(),
                    exploreQueryDto.getMinihomeId(),
                    exploreQueryDto.getTotalVisitorCnt(),
                    exploreQueryDto.getScore(),
                    exploreQueryDto.getLikeCount(),
                    exploreQueryDto.getCreatedAt());
        }
    }
}
