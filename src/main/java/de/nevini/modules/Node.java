package de.nevini.modules;

import de.nevini.util.Permissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.core.Permission;

@AllArgsConstructor
@Getter
@ToString
public enum Node {

    CORE_HELP("core.help", Module.CORE, Permissions.EVERYONE),
    CORE_MODULE_ACTIVATE("core.module.activate", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_MODULE_DEACTIVATE("core.module.deactivate", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_MODULE_GET("core.module.get", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_PERMISSION_ALLOW("core.permission.allow", Module.CORE, Permissions.MANAGE_PERMISSIONS),
    CORE_PERMISSION_DEBUG("core.permission.debug", Module.CORE, Permissions.MANAGE_PERMISSIONS),
    CORE_PERMISSION_DENY("core.permission.deny", Module.CORE, Permissions.MANAGE_PERMISSIONS),
    CORE_PERMISSION_GET("core.permission.get", Module.CORE, Permissions.MANAGE_PERMISSIONS),
    CORE_PERMISSION_RESET("core.permission.reset", Module.CORE, Permissions.MANAGE_PERMISSIONS),
    CORE_PING("core.ping", Module.CORE, Permissions.EVERYONE),
    CORE_PREFIX_GET("core.prefix.get", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_PREFIX_SET("core.prefix.set", Module.CORE, Permissions.MANAGE_SERVER),
    GUILD_ACTIVITY("guild.activity.game", Module.GUILD, Permissions.EVERYONE),
    ;

    private final String node;
    private final Module module;
    private final Permission[] defaultPermissions;

}
