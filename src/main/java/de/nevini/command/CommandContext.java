package de.nevini.command;

import de.nevini.listeners.EventDispatcher;
import de.nevini.services.common.*;
import de.nevini.services.external.OsuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@lombok.Value
@Component
public class CommandContext {

    private final boolean lockdown;
    private final String ownerId;
    private final String serverId;
    private final String serverInvite;

    private final Map<String, Command> commands;

    private final EventDispatcher eventDispatcher;

    private final ActivityService activityService;
    private final FeedService feedService;
    private final GameService gameService;
    private final IgnService ignService;
    private final ModuleService moduleService;
    private final OsuService osuService;
    private final PermissionService permissionService;
    private final PrefixService prefixService;

    public CommandContext(
            @Value("${bot.lockdown:true}") boolean lockdown,
            @Value("${bot.owner.id:#{null}}") String ownerId,
            @Value("${bot.server.id:#{null}}") String serverId,
            @Value("${bot.server.invite:#{null}}") String serverInvite,
            @Autowired ApplicationContext applicationContext,
            @Autowired EventDispatcher eventDispatcher,
            @Autowired ActivityService activityService,
            @Autowired FeedService feedService,
            @Autowired GameService gameService,
            @Autowired IgnService ignService,
            @Autowired ModuleService moduleService,
            @Autowired OsuService osuService,
            @Autowired PermissionService permissionService,
            @Autowired PrefixService prefixService
    ) {
        this.lockdown = lockdown;
        this.ownerId = ownerId;
        this.serverId = serverId;
        this.serverInvite = serverInvite;

        Map<String, Command> commands = new ConcurrentHashMap<>();
        applicationContext.getBeansOfType(Command.class).forEach((ignore, command) -> {
            log.info("Registering {}", command.getClass().getSimpleName());
            commands.put(command.getKeyword(), command);
            for (String alias : command.getAliases()) {
                commands.put(alias, command);
            }
        });
        this.commands = Collections.unmodifiableMap(commands);

        this.eventDispatcher = eventDispatcher;

        this.activityService = activityService;
        this.feedService = feedService;
        this.gameService = gameService;
        this.ignService = ignService;
        this.moduleService = moduleService;
        this.osuService = osuService;
        this.permissionService = permissionService;
        this.prefixService = prefixService;
    }

}
