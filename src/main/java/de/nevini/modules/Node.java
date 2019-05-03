package de.nevini.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Node {

    CORE_HELP("core.help"),
    CORE_MODULE_ACTIVATE("core.module.activate"),
    CORE_MODULE_DEACTIVATE("core.module.deactivate"),
    CORE_MODULE_GET("core.module.get"),
    CORE_PERMISSION_ALLOW("core.permission.allow"),
    CORE_PERMISSION_DEBUG("core.permission.debug"),
    CORE_PERMISSION_DENY("core.permission.deny"),
    CORE_PERMISSION_GET("core.permission.get"),
    CORE_PERMISSION_RESET("core.permission.reset"),
    CORE_PING("core.ping"),
    CORE_PREFIX_GET("core.prefix.get"),
    CORE_PREFIX_SET("core.prefix.set"),
    GUILD_ACTIVITY_GAME("guild.activity.game"),
    GUILD_ACTIVITY_USER("guild.activity.user");

    private final String node;

}
