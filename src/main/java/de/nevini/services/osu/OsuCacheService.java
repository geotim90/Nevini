package de.nevini.services.osu;

import de.nevini.api.osu.external.requests.OsuApiGetBeatmapsRequest;
import de.nevini.api.osu.services.OsuBeatmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class OsuCacheService {

    private final OsuBeatmapService beatmapService;

    private LocalDate startDate = null;
    private LocalDate nextDate = null;

    public OsuCacheService(@Autowired OsuBeatmapService beatmapService) {
        this.beatmapService = beatmapService;
    }

    @Scheduled(fixedDelay = 900000) // 15 minutes
    public void getChunk() {
        // check for date change
        if (!LocalDate.now().equals(startDate)) {
            startDate = LocalDate.now();
            nextDate = startDate;
        }
        // query date
        beatmapService.get(OsuApiGetBeatmapsRequest.builder()
                .since(Date.from(nextDate.atStartOfDay(ZoneOffset.UTC).toInstant()))
                .build());
        // increase date
        nextDate = nextDate.minusDays(1);
    }

}
