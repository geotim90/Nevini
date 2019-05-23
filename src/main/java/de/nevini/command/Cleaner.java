package de.nevini.command;

import de.nevini.listeners.EventDispatcher;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.TimeUnit;

import static de.nevini.util.Functions.ignore;

public class Cleaner {

    public static void tryDelete(Message message) {
        try {
            message.delete().queue(ignore(), ignore());
        } catch (Exception ignore) {
        }
    }

    public static void tryScheduleDelete(EventDispatcher eventDispatcher, Message message) {
        if (eventDispatcher != null && message != null) {
            eventDispatcher.schedule(1, TimeUnit.MINUTES, () -> tryDelete(message));
        }
    }

}
