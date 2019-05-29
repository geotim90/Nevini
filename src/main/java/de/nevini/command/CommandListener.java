package de.nevini.command;

import de.nevini.listeners.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static de.nevini.util.Formatter.summarize;

@Slf4j
@Component
public class CommandListener {

    private final CommandContext commandContext;

    public CommandListener(
            @Autowired CommandContext commandContext,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.commandContext = commandContext;
        eventDispatcher.subscribe(MessageReceivedEvent.class, this::onEvent);
    }

    private void onEvent(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && checkLockdown(event)) {
            String content = event.getMessage().getContentRaw();
            Optional<String> prefix = commandContext.getPrefixService().extractPrefix(event);
            if (prefix.isPresent()) {
                String[] args = content.substring(prefix.get().length()).trim().split("\\s+", 2);
                if (args.length == 0 || StringUtils.isEmpty(args[0])) {
                    callCommand(event, "help", args.length > 1 ? args[1] : null);
                } else {
                    callCommand(event, args[0].toLowerCase(), args.length > 1 ? args[1] : null);
                }
            } else if (!event.isFromType(ChannelType.TEXT)) {
                log.info("{} - Unknown command via direct message {}", event.getMessageId(),
                        summarize(event.getMessage().getContentRaw()));
            }
        }
    }

    private boolean checkLockdown(MessageReceivedEvent event) {
        if (commandContext.isLockdown()) {
            Guild guild = event.getGuild();
            // check for "home" server
            if (guild != null && !Objects.equals(guild.getId(), commandContext.getServerId())) {
                return false;
            }
            // only allow owner commands in lockdown mode
            return Objects.equals(event.getAuthor().getId(), commandContext.getOwnerId());
        } else {
            // not in lockdown
            return true;
        }
    }

    private void callCommand(MessageReceivedEvent event, String keyword, String argument) {
        Command command = commandContext.getCommands().get(keyword.toLowerCase());
        if (command != null) {
            log.info("{} - Calling {}", event.getMessageId(), command.getClass().getSimpleName());
            command.onEvent(new CommandEvent(commandContext, event, CommandOptions.parseArgument(argument)));
        } else {
            log.info("{} - No command found for keyword {} and argument {}", event.getMessageId(), keyword,
                    summarize(argument));
        }
    }

}
