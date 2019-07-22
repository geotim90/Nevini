package de.nevini.util.message;

import de.nevini.util.command.CommandReaction;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Adds two reactions to an existing {@link Message} with callbacks for each and a timeout of one minute.
 * Call {@link #decorate()} to make the magic happen.
 */
@RequiredArgsConstructor
public class ConfirmationDecorator {

    /**
     * The {@link Message} this decorator should attach to.
     */
    @NonNull
    private final Message message;

    /**
     * The only {@link User} that is allowed to interact with the decorated message.
     */
    @NonNull
    private final User user;

    /**
     * The {@link EventDispatcher} to use.
     */
    @NonNull
    private final EventDispatcher eventDispatcher;

    /**
     * The callback to call.
     * <ul>
     * <li>Receives {@link Boolean#TRUE} if the user accepted.</li>
     * <li>Receives {@link Boolean#FALSE} if the user rejected.</li>
     * <li>Receives {@code null} if the the confirmation timed out.</li>
     * </ul>
     */
    @NonNull
    private final Consumer<Boolean> confirmationCallback;

    public void decorate() {
        message.addReaction(CommandReaction.OK.getUnicode()).queue();
        message.addReaction(CommandReaction.ERROR.getUnicode()).queue();
        eventDispatcher.subscribe(MessageReactionAddEvent.class, event ->
                        message.getIdLong() == event.getMessageIdLong()
                                && user.getIdLong() == event.getUser().getIdLong(),
                event -> handleReaction(message, event), true,
                1, TimeUnit.MINUTES, () -> expire(message), false
        );
    }

    private void handleReaction(Message message, MessageReactionAddEvent event) {
        if (CommandReaction.OK.getUnicode().equals(event.getReactionEmote().getName())) {
            MessageCleaner.tryDelete(message);
            confirmationCallback.accept(Boolean.TRUE);
        } else if (CommandReaction.ERROR.getUnicode().equals(event.getReactionEmote().getName())) {
            MessageCleaner.tryDelete(message);
            confirmationCallback.accept(Boolean.FALSE);
        } else {
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }

    private void expire(Message message) {
        MessageCleaner.tryDelete(message);
        confirmationCallback.accept(null);
    }

}
