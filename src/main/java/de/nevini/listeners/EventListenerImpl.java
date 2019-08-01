package de.nevini.listeners;

import de.nevini.util.concurrent.EventDispatcher;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventListener} for JDA {@link Event} routing.<br>
 * Event types can be subscribed using the provided {@link EventDispatcher} methods.<br>
 * The event dispatcher is shut down if a {@link ShutdownEvent} is received.
 */
@Slf4j
@Component
public class EventListenerImpl extends EventDispatcher implements EventListener {

    public EventListenerImpl() {
        super(Runtime.getRuntime().availableProcessors());
        subscribe(ShutdownEvent.class, event -> shutdown());
    }

    @Override
    public void onEvent(@NonNull GenericEvent event) {
        publish(event);
    }

}
