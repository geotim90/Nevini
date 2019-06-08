package de.nevini.bot.listeners;

import de.nevini.commons.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventListener} for JDA {@link Event} routing.<br>
 * Event types can be subscribed using the provided {@link EventDispatcher} methods.<br>
 * The event dispatcher is shut down if a {@link ShutdownEvent} is received.
 */
@Slf4j
@Component
public class EventListenerImpl extends EventDispatcher<Event> implements EventListener {

    public EventListenerImpl() {
        super(Runtime.getRuntime().availableProcessors());
        subscribe(ShutdownEvent.class, event -> shutdown());
    }

    @Override
    public void onEvent(Event event) {
        publish(event);
    }

}
