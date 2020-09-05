package de.nevini.core.command;

import de.nevini.core.locators.Locatable;
import de.nevini.core.services.VersionService;
import de.nevini.modules.admin.game.services.GameService;
import de.nevini.modules.core.module.services.ModuleService;
import de.nevini.modules.core.permission.services.PermissionService;
import de.nevini.modules.core.prefix.services.PrefixService;
import de.nevini.modules.guild.activity.services.ActivityService;
import de.nevini.modules.guild.autorole.services.AutoRoleService;
import de.nevini.modules.guild.feed.services.FeedService;
import de.nevini.modules.guild.ign.services.IgnService;
import de.nevini.modules.guild.inactivity.services.InactivityService;
import de.nevini.modules.guild.tribute.services.TributeService;
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

    boolean lockdown;
    String ownerId;
    String serverId;
    String serverInvite;

    Map<String, Command> commands;
    EventDispatcher eventDispatcher;

    ActivityService activityService;
    AutoRoleService autoRoleService;
    FeedService feedService;
    GameService gameService;
    IgnService ignService;
    InactivityService inactivityService;
    ModuleService moduleService;
    PermissionService permissionService;
    PrefixService prefixService;
    TributeService tributeService;
    VersionService versionService;

    @Getter(AccessLevel.PRIVATE)
    ApplicationContext applicationContext;

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
            @Autowired ModuleService moduleService,
            @Autowired PermissionService permissionService,
            @Autowired PrefixService prefixService,
            @Autowired TributeService tributeService,
            @Autowired VersionService versionService
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
        this.moduleService = moduleService;
        this.permissionService = permissionService;
        this.prefixService = prefixService;
        this.tributeService = tributeService;
        this.versionService = versionService;

        this.applicationContext = applicationContext;
    }

    public <T extends Locatable> T locate(Class<T> type) {
        return applicationContext.getBeansOfType(type).values().stream().findFirst().orElse(null);
    }

}
