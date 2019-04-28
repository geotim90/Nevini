package de.nevini.command;

import de.nevini.EventDispatcher;
import de.nevini.services.EmoteService;
import de.nevini.services.ModuleService;
import de.nevini.services.PermissionService;
import de.nevini.services.PrefixService;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static de.nevini.util.FormatUtils.summarize;

@Slf4j
@Component
public class CommandListener {

    @Delegate
    private final CommandContext commandContext;

    public CommandListener(
            @Value("${bot.owner.id}") String ownerId,
            @Value("${bot.server.invite}") String serverInvite,
            @Autowired EventDispatcher eventDispatcher,
            @Autowired EmoteService emoteService,
            @Autowired ModuleService moduleService,
            @Autowired PermissionService permissionService,
            @Autowired ApplicationContext applicationContext,
            @Autowired PrefixService prefixService
    ) {
        Map<String, Command> commands = new ConcurrentHashMap<>();
        applicationContext.getBeansOfType(Command.class).forEach((ignore, command) -> {
            commands.put(command.getKeyword(), command);
            for (String alias : command.getAliases()) {
                commands.put(alias, command);
            }
        });

        commandContext = new CommandContext(ownerId, serverInvite, eventDispatcher, commands,
                prefixService, emoteService, moduleService, permissionService);

        eventDispatcher.subscribe(MessageReceivedEvent.class, this::onEvent);
    }

    public void onEvent(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String content = event.getMessage().getContentRaw();
            Optional<String> prefix = extractPrefix(event);
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

    private void callCommand(MessageReceivedEvent event, String keyword, String argument) {
        Command command = getCommands().get(keyword.toLowerCase());
        if (command != null) {
            log.info("{} - Calling {}", event.getMessageId(), command.getClass().getSimpleName());
            command.onEvent(new CommandEvent(event, commandContext, argument));
        } else {
            log.info("{} - No command found for keyword {} and argument {}", event.getMessageId(), keyword,
                    summarize(argument));
        }
    }

    private Optional<String> extractPrefix(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        String configuredPrefix = getPrefixService().getGuildPrefix(event.getGuild());
        if (content.startsWith(configuredPrefix)) {
            return Optional.of(configuredPrefix);
        } else if (content.startsWith("<@" + event.getJDA().getSelfUser().getId() + ">")) {
            return Optional.of("<@" + event.getJDA().getSelfUser().getId() + ">");
        } else if (content.startsWith("<@!" + event.getJDA().getSelfUser().getId() + ">")) {
            return Optional.of("<@!" + event.getJDA().getSelfUser().getId() + ">");
        } else if (content.startsWith("@" + event.getJDA().getSelfUser().getName())) {
            return Optional.of("@" + event.getJDA().getSelfUser().getName());
        } else if (event.isFromType(ChannelType.TEXT)
                && content.startsWith("@" + event.getGuild().getSelfMember().getEffectiveName())) {
            return Optional.of("@" + event.getGuild().getSelfMember().getEffectiveName());
        } else {
            return event.isFromType(ChannelType.TEXT) ? Optional.empty() : Optional.of(StringUtils.EMPTY);
        }
    }

}
