package de.nevini.modules.osu.services;

import de.nevini.modules.osu.api.requests.OsuApiRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OsuAsyncService {

    private final Map<OsuApiRequest<?>, Runnable> backlog = new ConcurrentHashMap<>();

    public void addTask(@NonNull OsuApiRequest<?> request, @NonNull Runnable task) {
        if (backlog.put(request, task) == null) {
            log.debug("Request queued: {}", request);
        }
    }

    @Scheduled(fixedDelay = 60000) // 1 minute
    private void processBacklog() {
        if (!backlog.isEmpty()) {
            OsuApiRequest<?> request = backlog.keySet().iterator().next();
            log.debug("Request dequeued: {}", request);
            backlog.remove(request).run();
        }
    }

}
