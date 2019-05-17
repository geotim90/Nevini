package de.nevini.command;

import de.nevini.listeners.EventDispatcher;
import de.nevini.services.*;
import lombok.Value;

import java.util.Map;

@Value
public class CommandContext {

    private final String ownerId;
    private final String serverInvite;
    private final EventDispatcher eventDispatcher;
    private final Map<String, Command> commands;

    private final ActivityService activityService;
    private final GameService gameService;
    private final ModuleService moduleService;
    private final PermissionService permissionService;
    private final PrefixService prefixService;

    private final LegacyActivityService legacyActivityService;
    private final LegacyContributionService legacyContributionService;
    private final LegacyTimeoutService legacyTimeoutService;

}
