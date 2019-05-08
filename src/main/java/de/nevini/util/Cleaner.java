package de.nevini.util;

import de.nevini.command.CommandEvent;
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

    public static void tryScheduleDelete(CommandEvent event) {
        if (event != null) {
            event.getEventDispatcher().schedule(1, TimeUnit.MINUTES, () -> tryDelete(event.getMessage()));
        }
    }

}
