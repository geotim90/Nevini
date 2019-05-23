package de.nevini.listeners;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HealthListener {

    public HealthListener(@Autowired EventDispatcher eventDispatcher) {
        eventDispatcher.subscribe(DisconnectEvent.class, this::logDisconnect);
        eventDispatcher.subscribe(ExceptionEvent.class, this::logException);
        eventDispatcher.subscribe(ReadyEvent.class, this::logReady);
        eventDispatcher.subscribe(ReconnectedEvent.class, ignore -> logReconnected());
        eventDispatcher.subscribe(ResumedEvent.class, ignore -> logResumed());
        eventDispatcher.subscribe(ShutdownEvent.class, this::logShutdown);
    }

    private void logDisconnect(DisconnectEvent event) {
        log.warn("Disconnected at {}", event.getDisconnectTime());
    }

    private void logException(ExceptionEvent event) {
        if (!event.isLogged()) {
            log.warn("Exception: ", event.getCause());
        }
    }

    private void logReady(ReadyEvent event) {
        log.info("Ready with {} guilds", event.getGuildTotalCount());
    }

    private void logReconnected() {
        log.info("Reconnected");
    }

    private void logResumed() {
        log.info("Resumed");
    }

    private void logShutdown(ShutdownEvent event) {
        log.info("Shutdown at {}", event.getShutdownTime());
    }

}
