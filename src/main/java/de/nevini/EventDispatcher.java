package de.nevini;


import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementation of {@link EventListener} for JDA {@link Event} routing.<br>
 * Event types can be subscribed using the provided {@code subscribe} methods.<br>
 * All executors are shutdown if a {@link ShutdownEvent} is received.
 */
@Slf4j
@Component
public class EventDispatcher implements EventListener {

    private final ScheduledExecutorService scheduledExecutorService;
    private final ExecutorService executorService;

    private final MultiValuedMap<Class<?>, EventTypeCallback<?>> callbacks;

    public EventDispatcher() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        executorService = Executors.newCachedThreadPool();
        callbacks = new ArrayListValuedHashMap<>();

        subscribe(ShutdownEvent.class, event -> shutdown());
    }

    /**
     * Subscribes events of a specific type (or any sub-class of said type).<br>
     * An optional condition can be provided, as well as a flag to only trigger the callback at most once.<br>
     * Timeout information can also be provided that will trigger a separate callback.<br>
     * <br>
     * The {@code eventType} and {@code callback} parameters are mandatory (i.e. cannot be {@code null}).<br>
     * If no {@code condition} is provided, events will only be filtered by their type.<br>
     * If {@code onlyOnce} is {@code true}, the {@code callback} will be called at most once.<br>
     * If {@code onlyOnce} is {@code true}, the {@code timeoutCallback} will only be called if the {@code callback}
     * was not called or {@code timeoutAlways} is {@code true}.<br>
     * If {@code timeout} is not positive or {@code timeoutUnit} is {@code null}, no timeout handling will be
     * executed.<br>
     * If {@code timeoutCallback} is not {@code null}, it will be called after the timeout expires under the
     * aforementioned conditions.
     *
     * @param eventType       the {@link Event} type to subscribe to
     * @param condition       the condition for filtering events (nullable)
     * @param callback        the callback for when a matching event is fired
     * @param onlyOnce        whether or not the callback should be executed at most once
     * @param timeout         the timeout value
     * @param timeoutUnit     the timeout unit (nullable)
     * @param timeoutCallback the callback for when the timeout expires (nullable)
     * @param timeoutAlways   whether or not the {@code timeoutCallback} should be called even if {@code callback} was
     *                        called successfully
     * @param <T>             the {@link Event} type
     */
    public synchronized <T extends Event> void subscribe(
            @NonNull Class<T> eventType, Predicate<T> condition, @NonNull Consumer<T> callback, boolean onlyOnce,
            long timeout, TimeUnit timeoutUnit, Runnable timeoutCallback, boolean timeoutAlways
    ) {
        if (!scheduledExecutorService.isShutdown()) {
            log.info("Registering {} subscription", eventType.getSimpleName());
            EventTypeCallback<T> eventTypeCallback = new EventTypeCallback<>(eventType, condition, callback, onlyOnce);
            callbacks.put(eventType, eventTypeCallback);
            if (timeout > 0 && timeoutUnit != null) {
                scheduledExecutorService.schedule(() -> {
                    if ((callbacks.removeMapping(eventType, callback) || timeoutAlways) && timeoutCallback != null) {
                        timeoutCallback.run();
                    }
                }, timeout, timeoutUnit);
            }
        }
    }

    /**
     * Subscribes events of a specific type (or any sub-class of said type).<br>
     * <br>
     * This method is provided as a convenience method for subscriptions with no condition or timeout.
     *
     * @param eventType the {@link Event} type to subscribe to
     * @param callback  the callback for when a matching event is fired
     * @param <T>       the {@link Event} type
     * @see #subscribe(Class, Predicate, Consumer, boolean, long, TimeUnit, Runnable, boolean)
     */
    public <T extends Event> void subscribe(Class<T> eventType, Consumer<T> callback) {
        subscribe(eventType, null, callback, false, 0, null, null, false);
    }

    private synchronized void shutdown() {
        log.info("Shutting down");
        scheduledExecutorService.shutdown();
        executorService.shutdown();
    }

    @Override
    public synchronized void onEvent(@NonNull Event event) {
        if (!executorService.isShutdown()) {
            log.debug("Received {} {}", event.getClass().getSimpleName(), event.getResponseNumber());
            executorService.execute(() -> dispatchEvent(event));
        }
    }

    private void dispatchEvent(Event event) {
        log.debug("Dispatching {} {}", event.getClass().getSimpleName(), event.getResponseNumber());
        Class<?> eventType = event.getClass();
        while (eventType != null) {
            if (callbacks.containsKey(eventType)) {
                for (EventTypeCallback<?> callback : callbacks.get(eventType)) {
                    log.debug("Calling callback for {} {}", event.getClass().getSimpleName(), event.getResponseNumber());
                    if (callback.onEvent(event) && callback.isOnlyOnce()) {
                        callbacks.removeMapping(eventType, callback);
                    }
                }
            }
            eventType = eventType.getSuperclass();
        }
    }

    @Value
    private static class EventTypeCallback<T extends Event> {

        @NonNull
        private final Class<T> eventType;
        private final Predicate<T> condition;
        @NonNull
        private final Consumer<T> callback;
        private final boolean onlyOnce;

        boolean onEvent(Event event) {
            if (eventType.isInstance(event)) {
                T castEvent = eventType.cast(event);
                if (condition == null || condition.test(castEvent)) {
                    callback.accept(castEvent);
                    return true;
                }
            }
            return false;
        }

    }

}
