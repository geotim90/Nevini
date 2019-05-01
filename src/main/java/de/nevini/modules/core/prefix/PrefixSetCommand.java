package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.util.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

public class PrefixSetCommand extends Command {

    public PrefixSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .module(Module.CORE)
                .node(Node.CORE_PREFIX_SET)
                .description("configures the command prefix")
                .syntax("<prefix>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String prefix = event.getArgument();
        if (StringUtils.isEmpty(prefix)) {
            if (event.canTalk()) {
                event.reply("What would you like the command prefix to be?",
                        message -> waitForPrefix(event, message));
            } else {
                event.reply(Emote.WARNING, "You did not specify a command prefix!");
            }
        } else if (!prefix.matches("\\S{1,32}")) {
            event.reply(Emote.WARNING,
                    "The command prefix cannot be longer than 32 characters and must not contain spaces!");
        } else {
            event.getPrefixService().setGuildPrefix(event.getGuild(), prefix);
            event.reply(Emote.OK);
        }
    }

    private void waitForPrefix(CommandEvent event, Message message) {
        event.getEventDispatcher().subscribe(MessageReceivedEvent.class,
                e -> e.getChannel().getId().equals(event.getChannel().getId())
                        && e.getAuthor().getId().equals(event.getAuthor().getId()),
                e -> acceptPrefix(event, message, e),
                true,
                1,
                TimeUnit.MINUTES,
                () -> expire(event, message),
                false
        );
    }

    private void acceptPrefix(CommandEvent event, Message message, MessageReceivedEvent e) {
        if (event.getPrefixService().extractPrefix(e).isPresent()) {
            expire(event, message);
        } else {
            String prefix = e.getMessage().getContentRaw();
            if (StringUtils.isEmpty(prefix)) {
                expire(event, message);
            } else if (!prefix.matches("\\S{1,32}")) {
                event.replyTo(e.getMessage(), Emote.WARNING,
                        "The command prefix cannot be longer than 32 characters and must not contain spaces!");
            } else {
                event.getPrefixService().setGuildPrefix(event.getGuild(), prefix);
                event.replyTo(e.getMessage(), Emote.OK);
            }
        }
    }

    private void expire(CommandEvent event, Message message) {
        event.replyTo(message, Emote.WARNING, "You did not specify a command prefix!");
    }

}
