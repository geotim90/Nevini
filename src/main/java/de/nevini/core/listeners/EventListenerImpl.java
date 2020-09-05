package de.nevini.core.listeners;

import de.nevini.util.concurrent.EventDispatcher;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EventListener} for JDA {@link Event} routing.<br>
 * Event types can be subscribed using the provided {@link EventDispatcher} methods.<br>
 * The event dispatcher is shut down if a {@link ShutdownEvent} is received.
 */
@Slf4j
@Component
public class EventListenerImpl extends EventDispatcher implements EventListener {

    private final ConfigurableApplicationContext applicationContext;

    public EventListenerImpl(@Autowired ConfigurableApplicationContext applicationContext) {
        super(Runtime.getRuntime().availableProcessors());
        this.applicationContext = applicationContext;
        subscribe(ShutdownEvent.class, event -> shutdown());
    }

    @Override
    public void onEvent(@NonNull GenericEvent event) {
        publish(event);
    }

    @Override
    public synchronized void shutdown() {
        super.shutdown();
        applicationContext.close();
    }

}
