package de.nevini.bot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.CommandListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CommandLogger implements CommandListener {

    @Override
    public void onCommand(CommandEvent event, Command command) {
        log.info("{} - {} received: {}",
                event.getMessage().getId(),
                command.getClass().getSimpleName(),
                StringUtils.abbreviate(event.getMessage().getContentRaw(), 50));
    }

    @Override
    public void onCompletedCommand(CommandEvent event, Command command) {
        log.info("{} - {} completed",
                event.getMessage().getId(),
                command.getClass().getSimpleName());
    }

    @Override
    public void onTerminatedCommand(CommandEvent event, Command command) {
        log.info("{} - {} terminated",
                event.getMessage().getId(),
                command.getClass().getSimpleName());
    }

    @Override
    public void onNonCommandMessage(MessageReceivedEvent event) {
        if (log.isDebugEnabled()) log.debug("{} - not a command: {}",
                event.getMessageId(),
                StringUtils.abbreviate(event.getMessage().getContentRaw(), 50));
    }

    @Override
    public void onCommandException(CommandEvent event, Command command, Throwable throwable) {
        log.warn("{} - {} failed: ",
                event.getMessage().getId(),
                command.getClass().getSimpleName(),
                throwable);
    }

}
