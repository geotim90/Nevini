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
    GUILD_ACTIVITY_USER("guild.activity.user"),
    LEGACY_ADD_ROLE("legacy.add.role"),
    LEGACY_GET_CONTRIBUTION("legacy.get.contribution"),
    LEGACY_GET_GAME("legacy.get.game"),
    LEGACY_GET_MEMBER("legacy.get.member"),
    LEGACY_GET_ROLE("legacy.get.role"),
    LEGACY_GET_TIMEOUT("legacy.get.timeout"),
    LEGACY_REMOVE_ROLE("legacy.remove.role"),
    LEGACY_REPORT("legacy.report"),
    LEGACY_SET_CONTRIBUTION("legacy.set.contribution"),
    LEGACY_SET_GAME("legacy.set.game"),
    LEGACY_SET_MEMBER("legacy.set.member"),
    LEGACY_SET_TIMEOUT("legacy.set.timeout"),
    LEGACY_UNSET_CONTRIBUTION("legacy.unset.contribution"),
    LEGACY_UNSET_GAME("legacy.unset.game"),
    LEGACY_UNSET_MEMBER("legacy.unset.member"),
    LEGACY_UNSET_TIMEOUT("legacy.unset.timeout"),
    ;

    private final String node;

}
