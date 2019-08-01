package de.nevini.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.Permission;

@AllArgsConstructor
@Getter
@ToString
public enum Node {

    CORE_HELP("core.help", Module.CORE, Permissions.EVERYONE),
    CORE_MODULE_ACTIVATE("core.module.activate", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_MODULE_DEACTIVATE("core.module.deactivate", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_MODULE_GET("core.module.get", Module.CORE, Permissions.MANAGE_SERVER),
    CORE_PERMISSION_ALLOW("core.permission.allow", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PERMISSION_DEBUG("core.permission.debug", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PERMISSION_DENY("core.permission.deny", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PERMISSION_GET("core.permission.get", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PERMISSION_RESET("core.permission.reset", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PING("core.ping", Module.CORE, Permissions.EVERYONE),
    CORE_PREFIX_GET("core.prefix.get", Module.CORE, Permissions.EVERYONE),
    CORE_PREFIX_SET("core.prefix.set", Module.CORE, Permissions.MANAGE_SERVER),
    GUILD_ACTIVITY_GET("guild.activity.get", Module.GUILD, Permissions.EVERYONE),
    GUILD_ACTIVITY_SET("guild.activity.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_AUTO_ROLE_GET("guild.auto-role.get", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_AUTO_ROLE_SET("guild.auto-role.set", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_AUTO_ROLE_UNSET("guild.auto-role.unset", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_FEED_GET("guild.feed.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_FEED_SET("guild.feed.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_FEED_UNSET("guild.feed.unset", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_FIND_CHANNEL("guild.find.channel", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_GAME("guild.find.game", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_MODULE("guild.find.module", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_FIND_NODE("guild.find.node", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_FIND_PERMISSION("guild.find.permission", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_FIND_ROLE("guild.find.role", Module.GUILD, Permissions.EVERYONE),
    GUILD_FIND_USER("guild.find.user", Module.GUILD, Permissions.EVERYONE),
    GUILD_IGN_GET("guild.ign.get", Module.GUILD, Permissions.EVERYONE),
    GUILD_IGN_SET("guild.ign.set", Module.GUILD, Permissions.MANAGE_NICKNAMES),
    GUILD_INACTIVITY_GET("guild.inactivity.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_INACTIVITY_SET("guild.inactivity.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_INACTIVITY_UNSET("guild.inactivity.unset", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_REPORT_ROLE("guild.report.role", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_REPORT_SELF("guild.report.self", Module.GUILD, Permissions.EVERYONE),
    GUILD_REPORT_SERVER("guild.report.server", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_REPORT_USER("guild.report.user", Module.GUILD, Permissions.MANAGE_SERVER),
    OSU_BEATMAP("osu.beatmap", Module.OSU, Permissions.EVERYONE),
    OSU_BEST("osu.best", Module.OSU, Permissions.EVERYONE),
    OSU_EVENTS("osu.events", Module.OSU, Permissions.EVERYONE),
    OSU_EVENTS_FEED(GUILD_FEED_SET.node, Module.OSU, GUILD_FEED_SET.defaultPermissions),
    OSU_LEADERBOARD("osu.leaderboard", Module.OSU, Permissions.EVERYONE),
    OSU_RECENT("osu.recent", Module.OSU, Permissions.EVERYONE),
    OSU_RECENT_FEED(GUILD_FEED_SET.node, Module.OSU, GUILD_FEED_SET.defaultPermissions),
    OSU_SCORES("osu.scores", Module.OSU, Permissions.EVERYONE),
    OSU_STATS("osu.stats", Module.OSU, Permissions.EVERYONE),
    WARFRAME_ORDER_BOOK("warframe.order-book", Module.WARFRAME, Permissions.EVERYONE),
    WARFRAME_PRICE_CHECK("warframe.price-check", Module.WARFRAME, Permissions.EVERYONE);

    private final String node;
    private final Module module;
    private final Permission[] defaultPermissions;

}
