package de.nevini.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Node {

    CORE_HELP("core.help"),
    CORE_PING("core.ping"),
    CORE_PREFIX_GET("core.prefix.get"),
    CORE_PREFIX_SET("core.prefix.set"),
    GUILD_ACTIVITY_GAME("guild.activity.game"),
    GUILD_ACTIVITY_USER("guild.activity.user");

    private final String node;

}
