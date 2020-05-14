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
    CORE_PERMISSION_OVERRIDES("core.permission.overrides", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PERMISSION_RESET("core.permission.reset", Module.CORE, Permissions.MANAGE_ROLES),
    CORE_PING("core.ping", Module.CORE, Permissions.EVERYONE),
    CORE_PREFIX_GET("core.prefix.get", Module.CORE, Permissions.EVERYONE),
    CORE_PREFIX_SET("core.prefix.set", Module.CORE, Permissions.MANAGE_SERVER),
    GEOBOT_USER("geobot.user", Module.GEOBOT, Permissions.EVERYONE),
    GEOBOT_MOD("geobot.mod", Module.GEOBOT, Permissions.MANAGE_SERVER),
    GEOBOT_ADMIN("geobot.admin", Module.GEOBOT, Permissions.MANAGE_SERVER),
    GUILD_ACTIVITY_GET("guild.activity.get", Module.GUILD, Permissions.EVERYONE),
    GUILD_ACTIVITY_SET("guild.activity.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_AUTO_MOD_GHOST_PING_IMMUNITY("guild.auto-mod.ghost-ping.immunity", Module.GUILD, Permissions.EVERYONE),
    GUILD_AUTO_ROLE_GET("guild.auto-role.get", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_AUTO_ROLE_JOIN("guild.auto-role.join", Module.GUILD, Permissions.EVERYONE),
    GUILD_AUTO_ROLE_PLAYING("guild.auto-role.playing", Module.GUILD, Permissions.EVERYONE),
    GUILD_AUTO_ROLE_SET("guild.auto-role.set", Module.GUILD, Permissions.MANAGE_ROLES),
    GUILD_AUTO_ROLE_VETERAN("guild.auto-role.veteran", Module.GUILD, Permissions.EVERYONE),
    GUILD_AUTO_ROLE_VOICE("guild.auto-role.voice", Module.GUILD, Permissions.EVERYONE),
    GUILD_FEED_GET("guild.feed.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_FEED_SET("guild.feed.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_IGN_GET("guild.ign.get", Module.GUILD, Permissions.EVERYONE),
    GUILD_IGN_SET("guild.ign.set", Module.GUILD, Permissions.MANAGE_NICKNAMES),
    GUILD_INACTIVITY_GET("guild.inactivity.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_INACTIVITY_SET("guild.inactivity.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_REPORT_ROLE("guild.report.role", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_REPORT_SELF("guild.report.self", Module.GUILD, Permissions.EVERYONE),
    GUILD_REPORT_SERVER("guild.report.server", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_REPORT_USER("guild.report.user", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_DELAY_GET("guild.tribute.delay.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_DELAY_SET("guild.tribute.delay.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_GET("guild.tribute.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_ROLE_GET("guild.tribute.role.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_ROLE_SET("guild.tribute.role.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_SET("guild.tribute.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_START_GET("guild.tribute.start.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_START_SET("guild.tribute.start.set", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_TIMEOUT_FEED(GUILD_FEED_SET.node, Module.GUILD, GUILD_FEED_SET.defaultPermissions),
    GUILD_TRIBUTE_TIMEOUT_GET("guild.tribute.timeout.get", Module.GUILD, Permissions.MANAGE_SERVER),
    GUILD_TRIBUTE_TIMEOUT_SET("guild.tribute.timeout.set", Module.GUILD, Permissions.MANAGE_SERVER),
    OSU_BEATMAP("osu.beatmap", Module.OSU, Permissions.EVERYONE),
    OSU_BEST("osu.best", Module.OSU, Permissions.EVERYONE),
    OSU_EVENTS("osu.events", Module.OSU, Permissions.EVERYONE),
    OSU_EVENTS_FEED(GUILD_FEED_SET.node, Module.OSU, GUILD_FEED_SET.defaultPermissions),
    OSU_LEADERBOARD("osu.leaderboard", Module.OSU, Permissions.EVERYONE),
    OSU_RECENT("osu.recent", Module.OSU, Permissions.EVERYONE),
    OSU_RECENT_FEED(GUILD_FEED_SET.node, Module.OSU, GUILD_FEED_SET.defaultPermissions),
    OSU_SCORES("osu.scores", Module.OSU, Permissions.EVERYONE),
    OSU_STATS("osu.stats", Module.OSU, Permissions.EVERYONE),
    UTIL_DEBUG_PERMISSION("util.debug.permission", Module.UTIL, Permissions.MANAGE_SERVER),
    UTIL_FIND_CHANNEL("util.find.channel", Module.UTIL, Permissions.MANAGE_SERVER),
    UTIL_FIND_GAME("util.find.game", Module.UTIL, Permissions.EVERYONE),
    UTIL_FIND_MODULE("util.find.module", Module.UTIL, Permissions.MANAGE_SERVER),
    UTIL_FIND_NODE("util.find.node", Module.UTIL, Permissions.MANAGE_ROLES),
    UTIL_FIND_PERMISSION("util.find.permission", Module.UTIL, Permissions.MANAGE_ROLES),
    UTIL_FIND_ROLE("util.find.role", Module.UTIL, Permissions.MANAGE_ROLES),
    UTIL_FIND_USER("util.find.user", Module.UTIL, Permissions.EVERYONE),
    UTIL_UNICODE_HTML("util.unicode.html", Module.UTIL, Permissions.EVERYONE),
    UTIL_UNICODE_MORSE("util.unicode.morse", Module.UTIL, Permissions.EVERYONE),
    UTIL_UNICODE_OBFUSCATE("util.unicode.obfuscate", Module.UTIL, Permissions.EVERYONE),
    UTIL_UNICODE_TEXT("util.unicode.text", Module.UTIL, Permissions.EVERYONE),
    UTIL_UNICODE_URL("util.unicode.url", Module.UTIL, Permissions.EVERYONE),
    WARFRAME_BARO("warframe.baro", Module.WARFRAME, Permissions.EVERYONE),
    WARFRAME_CYCLE("warframe.cycle", Module.WARFRAME, Permissions.EVERYONE),
    WARFRAME_ORDER_BOOK("warframe.order-book", Module.WARFRAME, Permissions.EVERYONE),
    WARFRAME_PRICE_CHECK("warframe.price-check", Module.WARFRAME, Permissions.EVERYONE),
    WARFRAME_RIVEN("warframe.riven", Module.WARFRAME, Permissions.EVERYONE);

    private final String node;
    private final Module module;
    private final Permission[] defaultPermissions;

}
