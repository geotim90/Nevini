package de.nevini.modules.geobot;

import de.nevini.command.Command;
import de.nevini.command.CommandContext;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptions;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class GeobotAdapter {

    private final String geobotId;

    private final CommandContext commandContext;

    public GeobotAdapter(
            @Value("${geobot.id:#{null}}") String geobotId,
            @Autowired CommandContext commandContext,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.geobotId = geobotId;
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
        String prefix = commandContext.getPrefixService().extractSiblingPrefix(event, geobotId);
        if (prefix == null) {
            return;
        }

        // locate command
        Command command = commandContext.getCommands().get("geobot");
        if (command != null) {
            // remove the bot prefix from the content
            String content = event.getMessage().getContentRaw().substring(StringUtils.defaultString(prefix).length()).trim();

            // execute command
            log.info("Executing command {} for input: {}", command.getClass().getSimpleName(), content);
            command.onEvent(new CommandEvent(commandContext, event, CommandOptions.parseArgument(content)));
        }
    }

    private boolean checkLockdown(MessageReceivedEvent event) {
        if (commandContext.isLockdown()) {
            // only allow "home" server or direct message in lockdown mode
            if (event.isFromGuild() && !Objects.equals(event.getGuild().getId(), commandContext.getServerId())) {
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
