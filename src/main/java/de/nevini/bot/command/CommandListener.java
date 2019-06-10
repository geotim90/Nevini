package de.nevini.bot.command;

import de.nevini.commons.concurrent.EventDispatcher;
import de.nevini.framework.command.CommandOptions;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class CommandListener {

    private final CommandContext commandContext;

    public CommandListener(
            @Autowired CommandContext commandContext,
            @Autowired EventDispatcher<Event> eventDispatcher
    ) {
        this.commandContext = commandContext;
        eventDispatcher.subscribe(MessageReceivedEvent.class, this::onEvent);
    }

    private void onEvent(MessageReceivedEvent event) {
        // ignore bot messages
        if (event.getAuthor().isBot()) {
            return;
        }

        // check for lockdown
        if (!checkLockdown(event)) {
            return;
        }

        // check bot prefix (e.g. mention or guild prefix)
        String prefix = commandContext.getPrefixService().extractPrefix(event);
        if (prefix == null) {
            return;
        }

        // remove the bot prefix from the content to check
        String trim = event.getMessage().getContentRaw().substring(StringUtils.defaultString(prefix).length()).trim();

        // ignore empty commands for default prefix and guild prefix
        if (StringUtils.isEmpty(trim) && (commandContext.getPrefixService().getDefaultPrefix().equals(prefix)
                || commandContext.getPrefixService().getGuildPrefix(event.getGuild()).equals(prefix)
        )) {
            return;
        }

        // default to help (only when mentioned)
        String content = StringUtils.defaultIfEmpty(trim, "help");

        // split the command keyword from the rest
        String[] args = content.split("\\s+", 2);

        // locate command
        Command command = commandContext.getCommands().get(args[0].toLowerCase());
        if (command != null) {
            // execute command
            log.info("Executing command {} for input: {}", command.getClass().getSimpleName(), content);
            command.onEvent(new CommandEvent(commandContext, event, CommandOptions.parseArgument(
                    args.length > 1 ? args[1] : StringUtils.EMPTY
            )));
        }
    }

    private boolean checkLockdown(MessageReceivedEvent event) {
        if (commandContext.isLockdown()) {
            Guild guild = event.getGuild();
            // only allow "home" server or direct message in lockdown mode
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

}
