package de.nevini.util;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class Confirmation {

    @NonNull
    private final CommandEvent context;
    @NonNull
    private final Message message;
    @NonNull
    private final Runnable callback;

    public void decorate() {
        message.addReaction(CommandReaction.OK.getUnicode()).queue();
        message.addReaction(CommandReaction.ERROR.getUnicode()).queue();
        context.getEventDispatcher().subscribe(MessageReactionAddEvent.class, event ->
                        message.getIdLong() == event.getMessageIdLong()
                                && context.getAuthor().getIdLong() == event.getUser().getIdLong(),
                event -> handleReaction(message, event), true,
                1, TimeUnit.MINUTES, () -> expire(message), false
        );
    }

    private void handleReaction(Message message, MessageReactionAddEvent event) {
        if (CommandReaction.OK.getUnicode().equals(event.getReactionEmote().getName())) {
            message.delete().queue();
            callback.run();
        } else if (CommandReaction.ERROR.getUnicode().equals(event.getReactionEmote().getName())) {
            message.delete().queue();
            replyCancelled(context);
        } else {
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }

    private void expire(Message message) {
        message.delete().queue();
        replyExpired(context);
    }

    private void replyCancelled(CommandEvent event) {
        event.reply(event.getAuthor().getAsMention() + ", your previous command was cancelled.", event::complete);
    }

    private void replyExpired(CommandEvent event) {
        event.reply(event.getAuthor().getAsMention() + ", your previous command expired.", event::complete);
    }

}
