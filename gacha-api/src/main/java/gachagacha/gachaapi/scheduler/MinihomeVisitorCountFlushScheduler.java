package gachagacha.gachaapi.scheduler;

import gachagacha.domain.minihome.MinihomeRepository;
import gachagacha.storageredis.MinihomeRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinihomeVisitorCountFlushScheduler {

    private final MinihomeRedisRepository minihomeRedisRepository;
    private final MinihomeRepository minihomeRepository;

    @Scheduled(fixedRate = 30000)
    public void publishOutboxMessage() {
        Map<Object, Object> minihomeMap =
                minihomeRedisRepository.getAllMinihomeIds();
        for (Object minihomeId : minihomeMap.keySet()) {
            Long minihomeIdLong = Long.valueOf(minihomeId.toString());
            int visitorCount = Integer.valueOf(minihomeMap.get(minihomeId).toString());
            minihomeRepository.updateVisitorCount(minihomeIdLong, visitorCount);
        }
    }
}
