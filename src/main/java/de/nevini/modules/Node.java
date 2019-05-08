package de.nevini.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Node {

    CORE_HELP("core.help", Module.CORE),
    CORE_MODULE_ACTIVATE("core.module.activate", Module.CORE),
    CORE_MODULE_DEACTIVATE("core.module.deactivate", Module.CORE),
    CORE_MODULE_GET("core.module.get", Module.CORE),
    CORE_PERMISSION_ALLOW("core.permission.allow", Module.CORE),
    CORE_PERMISSION_DEBUG("core.permission.debug", Module.CORE),
    CORE_PERMISSION_DENY("core.permission.deny", Module.CORE),
    CORE_PERMISSION_GET("core.permission.get", Module.CORE),
    CORE_PERMISSION_RESET("core.permission.reset", Module.CORE),
    CORE_PING("core.ping", Module.CORE),
    CORE_PREFIX_GET("core.prefix.get", Module.CORE),
    CORE_PREFIX_SET("core.prefix.set", Module.CORE),
    GUILD_ACTIVITY_GAME("guild.activity.game", Module.GUILD),
    GUILD_ACTIVITY_USER("guild.activity.user", Module.GUILD),
    LEGACY_ADD_ROLE("legacy.add.role", Module.LEGACY),
    LEGACY_GET_CONTRIBUTION("legacy.get.contribution", Module.LEGACY),
    LEGACY_GET_GAME("legacy.get.game", Module.LEGACY),
    LEGACY_GET_MEMBER("legacy.get.member", Module.LEGACY),
    LEGACY_GET_ROLE("legacy.get.role", Module.LEGACY),
    LEGACY_GET_TIMEOUT("legacy.get.timeout", Module.LEGACY),
    LEGACY_REMOVE_ROLE("legacy.remove.role", Module.LEGACY),
    LEGACY_REPORT("legacy.report", Module.LEGACY),
    LEGACY_SET_CONTRIBUTION("legacy.set.contribution", Module.LEGACY),
    LEGACY_SET_GAME("legacy.set.game", Module.LEGACY),
    LEGACY_SET_MEMBER("legacy.set.member", Module.LEGACY),
    LEGACY_SET_TIMEOUT("legacy.set.timeout", Module.LEGACY),
    LEGACY_UNSET_CONTRIBUTION("legacy.unset.contribution", Module.LEGACY),
    LEGACY_UNSET_GAME("legacy.unset.game", Module.LEGACY),
    LEGACY_UNSET_MEMBER("legacy.unset.member", Module.LEGACY),
    LEGACY_UNSET_TIMEOUT("legacy.unset.timeout", Module.LEGACY),
    ;

    private final String node;
    private final Module module;

}
