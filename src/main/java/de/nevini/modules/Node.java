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
    LEGACY_ADD_ROLE("legacy.add.role", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_GET_CONTRIBUTION("legacy.get.contribution", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_GET_GAME_TIMEOUT("legacy.get.game.timeout", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_GET_MEMBER("legacy.get.member", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_GET_ROLE("legacy.get.role", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_GET_TIMEOUT("legacy.get.timeout", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_REMOVE_ROLE("legacy.remove.role", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_REPORT("legacy.report", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_SET_CONTRIBUTION("legacy.set.contribution", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_SET_GAME_TIMEOUT("legacy.set.game.timeout", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_SET_MEMBER("legacy.set.member", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_SET_TIMEOUT("legacy.set.timeout", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_UNSET_CONTRIBUTION("legacy.unset.contribution", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_UNSET_GAME_TIMEOUT("legacy.unset.game.timeout", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_UNSET_MEMBER("legacy.unset.member", Module.LEGACY, Permissions.MANAGE_SERVER),
    LEGACY_UNSET_TIMEOUT("legacy.unset.timeout", Module.LEGACY, Permissions.MANAGE_SERVER),
    ;

    private final String node;
    private final Module module;
    private final Permission[] defaultPermissions;

}
