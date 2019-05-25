package de.nevini.command;

import de.nevini.listeners.EventDispatcher;
import de.nevini.services.common.*;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static de.nevini.util.Formatter.summarize;

@Slf4j
@Component
public class CommandListener {

    @Delegate
    private final CommandContext commandContext;

    public CommandListener(
            @Value("${bot.lockdown:true}") boolean lockdown,
            @Value("${bot.owner.id:#{null}}") String ownerId,
            @Value("${bot.server.id:#{null}}") String serverId,
            @Value("${bot.server.invite:#{null}}") String serverInvite,
            @Autowired ApplicationContext applicationContext,
            @Autowired EventDispatcher eventDispatcher,
            @Autowired ActivityService activityService,
            @Autowired GameService gameService,
            @Autowired IgnService ignService,
            @Autowired ModuleService moduleService,
            @Autowired PermissionService permissionService,
            @Autowired PrefixService prefixService
    ) {
        Map<String, Command> commands = new ConcurrentHashMap<>();
        applicationContext.getBeansOfType(Command.class).forEach((ignore, command) -> {
            log.info("Registering {}", command.getClass().getSimpleName());
            commands.put(command.getKeyword(), command);
            for (String alias : command.getAliases()) {
                commands.put(alias, command);
            }
        });

        commandContext = new CommandContext(lockdown, ownerId, serverId, serverInvite, eventDispatcher, commands,
                activityService, gameService, ignService, moduleService, permissionService, prefixService);

        eventDispatcher.subscribe(MessageReceivedEvent.class, this::onEvent);
    }

    private void onEvent(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && checkLockdown(event)) {
            String content = event.getMessage().getContentRaw();
            Optional<String> prefix = getPrefixService().extractPrefix(event);
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
        Command command = getCommands().get(keyword.toLowerCase());
        if (command != null) {
            log.info("{} - Calling {}", event.getMessageId(), command.getClass().getSimpleName());
            command.onEvent(new CommandEvent(commandContext, event, CommandOptions.parseArgument(argument)));
        } else {
            log.info("{} - No command found for keyword {} and argument {}", event.getMessageId(), keyword,
                    summarize(argument));
        }
    }

}
