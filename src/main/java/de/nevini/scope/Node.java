package de.nevini.scope;

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
    CORE_PREFIX_GET("core.prefix.get", Module.CORE, Permissions.EVERYONE),
    CORE_PREFIX_SET("core.prefix.set", Module.CORE, Permissions.MANAGE_SERVER),
    GUILD_ACTIVITY("guild.activity", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_CHANNEL("guild.find.channel", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_GAME("guild.find.game", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_MODULE("guild.find.module", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_FIND_NODE("guild.find.node", Module.GUILD, Permissions.MANAGE_PERMISSIONS),
    GUILD_FIND_PERMISSION("guild.find.permission", Module.GUILD, Permissions.MANAGE_PERMISSIONS),
    GUILD_FIND_ROLE("guild.find.role", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_USER("guild.find.user", Module.GUILD, Permissions.EVERYONE),
    GUILD_IGN_GET("guild.ign.get", Module.GUILD, Permissions.EVERYONE),
    GUILD_IGN_SET("guild.ign.set", Module.GUILD, Permissions.MANAGE_NICKNAMES),
    OSU_ROLL("osu.roll", Module.OSU, Permissions.EVERYONE),
    OSU_STATS("osu.stats", Module.OSU, Permissions.EVERYONE),
    FEED_OSU_STATS("feed.osu.stats", Module.OSU, Permissions.MANAGE_SERVER),
    ;

    private final String node;
    private final Module module;
    private final Permission[] defaultPermissions;

}
