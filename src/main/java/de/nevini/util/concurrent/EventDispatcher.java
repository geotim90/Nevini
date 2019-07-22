package de.nevini.util.concurrent;


import lombok.NonNull;
import lombok.Value;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Dispatches events using a publisher-subscriber pattern. Specific event types
 * can be subscribed using {@link #subscribe(Class, Predicate, Consumer, boolean, long, TimeUnit, Runnable, boolean)}.
 * Events can be published using {@link #publish(Object)}. There are also methods for asynchronous task execution
 * ({@link #execute(Runnable)} and {@link #schedule(long, TimeUnit, Runnable)}. The internal executor services can be
 * shut down using {@link #shutdown()}.
 */
public class EventDispatcher {

    private final Map<Class<?>, Set<Subscription<?>>> subscriptions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Creates a new event dispatcher with a single-threaded {@link ScheduledExecutorService}.
     */
    public EventDispatcher() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Creates a new event dispatcher with a {@link ScheduledExecutorService} of the given size.
     *
     * @param threads the number of threads for the thread pool
     */
    public EventDispatcher(int threads) {
        scheduledExecutorService = Executors.newScheduledThreadPool(threads);
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
     * aforementioned conditions.<br>
     * <br>
     * Calling this method after calling {@link #shutdown()} will have no effect.
     *
     * @param eventType       the event type to subscribe to
     * @param condition       the condition for filtering events (nullable)
     * @param callback        the callback for when a matching event is fired
     * @param onlyOnce        whether or not the callback should be executed at most once
     * @param timeout         the timeout value
     * @param timeoutUnit     the timeout unit (nullable)
     * @param timeoutCallback the callback for when the timeout expires (nullable)
     * @param timeoutAlways   whether or not the {@code timeoutCallback} should be called even if {@code callback} was
     *                        called successfully
     * @param <T>             the event type
     * @throws NullPointerException if {@code eventType} or {@code callback} is {@code null}
     */
    public synchronized <T> void subscribe(
            @NonNull Class<T> eventType, Predicate<T> condition, @NonNull Consumer<T> callback, boolean onlyOnce,
            long timeout, TimeUnit timeoutUnit, Runnable timeoutCallback, boolean timeoutAlways
    ) {
        // only subscribe if not already shut down
        if (!scheduledExecutorService.isShutdown()) {
            // create a subscription from the provided parameters
            Subscription<T> subscription = new Subscription<>(eventType, condition, callback, onlyOnce);
            // add the subscription to the list for the provided event type
            synchronized (subscriptions) {
                subscriptions.computeIfAbsent(eventType, ignore -> new LinkedHashSet<>()).add(subscription);
            }
            // check if timeout handling is required
            if (timeout > 0 && timeoutUnit != null) {
                // schedule the timeout
                scheduledExecutorService.schedule(() -> {
                    synchronized (subscriptions) {
                        // remove the subscription on timeout
                        if ((subscriptions.get(eventType).remove(subscription) || timeoutAlways)
                                && timeoutCallback != null
                        ) {
                            // execute the timeout callback immediately
                            timeoutCallback.run();
                        }
                    }
                }, timeout, timeoutUnit);
            }
        }
    }

    /**
     * Subscribes events of a specific type (or any sub-class of said type).<br>
     * An optional condition can be provided, as well as a flag to only trigger the callback at most once.<br>
     * <br>
     * This method is provided as a convenience method for subscriptions with no timeout.
     *
     * @param eventType the event type to subscribe to
     * @param condition the condition for filtering events (nullable)
     * @param callback  the callback for when a matching event is fired
     * @param onlyOnce  whether or not the callback should be executed at most once
     * @param <T>       the event type
     * @throws NullPointerException if {@code eventType} or {@code callback} is {@code null}
     * @see #subscribe(Class, Predicate, Consumer, boolean, long, TimeUnit, Runnable, boolean)
     */
    public <T> void subscribe(
            Class<T> eventType, Predicate<T> condition, Consumer<T> callback, boolean onlyOnce
    ) {
        subscribe(eventType, condition, callback, onlyOnce, 0, null, null, false);
    }

    /**
     * Subscribes events of a specific type (or any sub-class of said type).<br>
     * <br>
     * This method is provided as a convenience method for subscriptions with no additional conditions or timeout.
     *
     * @param eventType the event type to subscribe to
     * @param callback  the callback for when a matching event is fired
     * @param <T>       the event type
     * @throws NullPointerException if {@code eventType} or {@code callback} is {@code null}
     * @see #subscribe(Class, Predicate, Consumer, boolean, long, TimeUnit, Runnable, boolean)
     */
    public <T> void subscribe(Class<T> eventType, Consumer<T> callback) {
        subscribe(eventType, null, callback, false, 0, null, null, false);
    }

    /**
     * Convenience method for executing asynchronous tasks.<br>
     * <br>
     * Calling this method after calling {@link #shutdown()} will have no effect.
     *
     * @param task the task to execute
     * @throws NullPointerException if {@code task} is {@code null}
     */
    public synchronized void execute(@NonNull Runnable task) {
        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.execute(task);
        }
    }

    /**
     * Convenience method for scheduling delayed tasks.<br>
     * <br>
     * Calling this method after calling {@link #shutdown()} will have no effect.
     *
     * @param timeout         the timeout value
     * @param timeoutUnit     the timeout unit
     * @param timeoutCallback the callback for when the timeout expires
     * @throws NullPointerException if {@code timeoutUnit} or {@code timeoutCallback} is {@code null}
     */
    public synchronized void schedule(long timeout, @NonNull TimeUnit timeoutUnit, @NonNull Runnable timeoutCallback) {
        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.schedule(timeoutCallback, timeout, timeoutUnit);
        }
    }

    /**
     * Publishes an event to all matching subscribers.<br>
     * <br>
     * Calling this method after calling {@link #shutdown()} will have no effect.
     *
     * @param event the event to publish
     * @throws NullPointerException if {@code event} is {@code null}
     */
    public synchronized void publish(@NonNull Object event) {
        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.execute(() -> dispatchEvent(event));
        }
    }

    /**
     * Dispatches an event once the scheduled executor service has the capacity to do so.
     *
     * @param event the event to dispatch
     * @throws NullPointerException if {@code event} is {@code null}
     *                              (should not occur due to the {@code null} check in {@link #publish(Object)})
     */
    private void dispatchEvent(Object event) {
        // get the event type
        Class<?> eventType = event.getClass();
        // iterate over event type class hierarchy
        while (eventType != null) {
            synchronized (subscriptions) {
                // check if there are any matching subscriptions
                if (subscriptions.containsKey(eventType)) {
                    // iterate over subscriptions and remove "only-once" subscriptions if called successfully
                    subscriptions.get(eventType).removeIf(callback -> callback.onEvent(event) && callback.isOnlyOnce());
                }
            }
            // get the next step in the class hierarchy
            eventType = eventType.getSuperclass();
        }
    }

    /**
     * Shuts down the {@link ScheduledExecutorService} in this event dispatcher.
     * <br>
     * Calling this method multiple times will have no effect.
     *
     * @see ExecutorService#shutdown()
     */
    public synchronized void shutdown() {
        scheduledExecutorService.shutdown();
    }

    /**
     * A subscription wraps the following four pieces of information:
     * <ul>
     * <li>{@code eventType} - the event type (including sub-classes) that this subscription will accept</li>
     * <li>{@code condition} (can be {@code null})
     * - a filter condition to check when calling {@link #onEvent(Object)}</li>
     * <li>{@code callback} - the callback to call if a matching event was received that passed {@code condition}</li>
     * <li>{@code onlyOnly}
     * - whether this subscription will expire once {@code callback} has been called successfully</li>
     * </ul>
     *
     * @param <T> the event type parameter for {@code eventType}, {@code condition} and {@code callback}
     */
    @Value
    private class Subscription<T> {

        @NonNull
        private final Class<T> eventType;
        private final Predicate<T> condition;
        @NonNull
        private final Consumer<T> callback;
        private final boolean onlyOnce;

        /**
         * Notifies this {@link Subscription} about a new, potentially matching event.
         *
         * @param event the event (nullable)
         * @return {@code true} if the event passed the filter {@code condition} and {@code callback} was called
         * successfully; {@code false} otherwise
         */
        boolean onEvent(Object event) {
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
