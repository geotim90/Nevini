package de.nevini.bot.command;

import de.nevini.commons.concurrent.EventDispatcher;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.Event;

import java.util.concurrent.TimeUnit;

import static de.nevini.commons.util.Functions.ignore;

public class Cleaner {

    public static void tryDelete(Message message) {
        try {
            message.delete().queue(ignore(), ignore());
        } catch (Exception ignore) {
        }
    }

    public static void tryScheduleDelete(EventDispatcher<Event> eventDispatcher, Message message) {
        if (eventDispatcher != null && message != null) {
            eventDispatcher.schedule(1, TimeUnit.MINUTES, () -> tryDelete(message));
        }
    }

}
