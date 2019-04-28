package de.nevini.command;

import de.nevini.EventDispatcher;
import de.nevini.services.EmoteService;
import de.nevini.services.ModuleService;
import de.nevini.services.PermissionService;
import de.nevini.services.PrefixService;
import lombok.Value;

import java.util.Map;

@Value
public class CommandContext {

    private final String ownerId;
    private final String serverInvite;
    private final EventDispatcher eventDispatcher;
    private final Map<String, Command> commands;
    private final PrefixService prefixService;
    private final EmoteService emoteService;
    private final ModuleService moduleService;
    private final PermissionService permissionService;

}
