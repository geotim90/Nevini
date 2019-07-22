package de.nevini.framework.message;

import de.nevini.commons.concurrent.EventDispatcher;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.TimeUnit;

import static de.nevini.commons.util.Functions.ignore;

/**
 * Utility class for cleaning up messages.
 */
public class MessageCleaner {

    /**
     * Attempts to delete the provided {@link Message} asynchronously and fails silently.
     * Does nothing if {@code message} is {@code null}.
     *
     * @param message the {@link Message} to delete
     */
    public static void tryDelete(Message message) {
        if (message != null) {
            try {
                // delete asynchronously and ignore all results
                message.delete().queue(ignore(), ignore());
            } catch (Exception ignore) {
                // ignore all exceptions too
            }
        }
    }

    /**
     * Attempts to delete the provided {@link Message} asynchronously after one minute.
     * Does nothing if {@code eventDispatcher} or {@code message} is {@code null}.
     *
     * @param eventDispatcher the {@link EventDispatcher} to use for scheduling
     * @param message         the {@link Message} to delete
     */
    public static void tryScheduleDelete(EventDispatcher eventDispatcher, Message message) {
        if (eventDispatcher != null && message != null) {
            eventDispatcher.schedule(1, TimeUnit.MINUTES, () -> tryDelete(message));
        }
    }

}
