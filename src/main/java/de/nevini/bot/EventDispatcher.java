package de.nevini.bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
@Component
public class EventDispatcher implements EventListener {

    @Getter
    private final EventWaiter eventWaiter = new EventWaiter();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final MultiValueMap<Class<?>, EventTypeCallback<?>> eventTypeCallbacks = new LinkedMultiValueMap<>();

    public EventDispatcher() {
        addEventListener(ShutdownEvent.class, e -> shutdown());
    }

    public <T extends Event> void addEventListener(Class<T> eventType, Consumer<? super T> callback) {
        eventTypeCallbacks.add(eventType, new EventTypeCallback<>(eventType, callback));
    }

    private synchronized void shutdown() {
        executorService.shutdown();
    }

    private synchronized boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public void onEvent(Event event) {
        if (!isShutdown()) {
            log.debug("Queuing {} {}", event.getClass().getSimpleName(), event.getResponseNumber());
            executorService.execute(() -> {
                log.debug("Dispatching {} {}", event.getClass().getSimpleName(), event.getResponseNumber());
                eventWaiter.onEvent(event);
                dispatchEvent(event);
            });
        }
    }

    private void dispatchEvent(Event event) {
        Class<?> eventType = event.getClass();
        while (eventType != null) {
            if (eventTypeCallbacks.containsKey(eventType)) {
                eventTypeCallbacks.get(eventType).parallelStream().forEach(e -> e.onEvent(event));
            }
            eventType = eventType.getSuperclass();
        }
    }

    @Value
    private static class EventTypeCallback<T extends Event> {
        private final Class<T> eventType;
        private final Consumer<? super T> callback;

        void onEvent(Object event) {
            callback.accept(eventType.cast(event));
        }
    }

}
