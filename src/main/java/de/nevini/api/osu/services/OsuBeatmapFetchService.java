package de.nevini.api.osu.services;

import de.nevini.api.osu.external.requests.OsuApiGetBeatmapsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class OsuBeatmapFetchService {

    private final OsuAsyncService asyncService;
    private final OsuBeatmapService beatmapService;

    private LocalDate startDate = null;
    private LocalDate nextDate = null;

    public OsuBeatmapFetchService(@Autowired OsuAsyncService asyncService, @Autowired OsuBeatmapService beatmapService) {
        this.asyncService = asyncService;
        this.beatmapService = beatmapService;
    }

    @Scheduled(fixedDelay = 900000) // 15 minutes
    private void addChunk() {
        // check for date change
        if (!LocalDate.now().equals(startDate)) {
            startDate = LocalDate.now();
            nextDate = startDate;
        }
        // build request
        OsuApiGetBeatmapsRequest request = OsuApiGetBeatmapsRequest.builder()
                .since(Date.from(nextDate.atStartOfDay(ZoneOffset.UTC).toInstant()))
                .build();
        asyncService.addTask(request, () -> beatmapService.get(request));
        // increase date
        nextDate = nextDate.minusDays(1);
    }

}
