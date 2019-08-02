package de.nevini.command;

import de.nevini.scope.Locatable;
import de.nevini.services.common.*;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.AccessLevel;
import lombok.Getter;
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
    private final AutoRoleService autoRoleService;
    private final FeedService feedService;
    private final GameService gameService;
    private final IgnService ignService;
    private final InactivityService inactivityService;
    private final MetricsService metricsService;
    private final ModuleService moduleService;
    private final PermissionService permissionService;
    private final PrefixService prefixService;
    private final TributeService tributeService;

    @Getter(AccessLevel.PRIVATE)
    private final ApplicationContext applicationContext;

    public CommandContext(
            @Value("${bot.lockdown:true}") boolean lockdown,
            @Value("${bot.owner.id:#{null}}") String ownerId,
            @Value("${bot.server.id:#{null}}") String serverId,
            @Value("${bot.server.invite:#{null}}") String serverInvite,
            @Autowired ApplicationContext applicationContext,
            @Autowired EventDispatcher eventDispatcher,
            @Autowired ActivityService activityService,
            @Autowired AutoRoleService autoRoleService,
            @Autowired FeedService feedService,
            @Autowired GameService gameService,
            @Autowired IgnService ignService,
            @Autowired InactivityService inactivityService,
            @Autowired MetricsService metricsService,
            @Autowired ModuleService moduleService,
            @Autowired PermissionService permissionService,
            @Autowired PrefixService prefixService,
            @Autowired TributeService tributeService
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
        this.autoRoleService = autoRoleService;
        this.feedService = feedService;
        this.gameService = gameService;
        this.ignService = ignService;
        this.inactivityService = inactivityService;
        this.metricsService = metricsService;
        this.moduleService = moduleService;
        this.permissionService = permissionService;
        this.prefixService = prefixService;
        this.tributeService = tributeService;

        this.applicationContext = applicationContext;
    }

    public <T extends Locatable> T locate(Class<T> type) {
        return applicationContext.getBeansOfType(type).values().stream().findFirst().orElse(null);
    }

}
