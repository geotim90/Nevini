package de.nevini.command;

import lombok.NonNull;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

public abstract class CommandWithRequiredArgument extends Command {

    private final String argumentName;

    protected CommandWithRequiredArgument(@NonNull CommandDescriptor descriptor, @NonNull String argumentName) {
        super(descriptor);
        this.argumentName = argumentName;
    }

    @Override
    protected void validate() {
        super.validate();
        if (getSyntax().isEmpty()) {
            throw new IllegalStateException("Syntax is empty.");
        }
        if (argumentName.isEmpty()) {
            throw new IllegalStateException("Argument name is empty.");
        }
    }

    @Override
    protected void execute(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgument())) {
            if (event.canTalk()) {
                event.reply("Please enter " + argumentName + " below:",
                        message -> waitForArgument(event, message));
            } else {
                expire(event, event.getMessage(), true);
            }
        } else {
            acceptArgument(event, event.getMessage(), event.getArgument());
        }
    }

    protected void waitForArgument(CommandEvent event, Message message) {
        event.getEventDispatcher().subscribe(MessageReceivedEvent.class,
                e -> e.getChannel().getId().equals(event.getChannel().getId())
                        && e.getAuthor().getId().equals(event.getAuthor().getId()),
                e -> {
                    if (event.getPrefixService().extractPrefix(e).isPresent()) {
                        expire(event, message, false);
                    } else if (StringUtils.isEmpty(e.getMessage().getContentRaw())) {
                        expire(event, message, true);
                    } else {
                        acceptArgument(event, e.getMessage(), e.getMessage().getContentRaw());
                    }
                },
                true,
                1,
                TimeUnit.MINUTES,
                () -> expire(event, message, false),
                false
        );
    }

    protected void expire(CommandEvent event, Message message, boolean error) {
        if (error) {
            event.replyTo(message, CommandReaction.WARNING, "You did not provide " + argumentName + "!");
        } else {
            event.replyTo(message, event.getAuthor().getAsMention() +
                    ", your previous command was cancelled or expired.");
        }
    }

    protected abstract void acceptArgument(CommandEvent event, Message message, String argument);

}
