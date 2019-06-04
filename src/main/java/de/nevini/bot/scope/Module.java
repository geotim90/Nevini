package de.nevini.bot.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * A module is a collection of commands.
 */
@AllArgsConstructor
@Getter
@ToString
public enum Module {

    CORE("core", true, true,
            "These commands are a core part of Nevini and are always available. "
                    + "In other words, this module is active by default and cannot be deactivated."),
    GUILD("guild", true, false,
            "These commands help manage a server (aka. guild) and monitor user (aka. member) activity. "
                    + "This modules is active by default but can be deactivated."),
    OSU("osu", false, false,
            "These commands are related to [osu!](https://osu.ppy.sh)."),
    ;

    /**
     * The name of the module.
     * Also acts as the key value to identify a module.
     */
    private final String name;

    /**
     * Whether this module is enabled by default.
     */
    private final boolean enabledByDefault;

    /**
     * Whether this module is always activate and cannot be deactivated.
     */
    private final boolean alwaysEnabled;

    /**
     * A description of this module for use in documentation.
     * Contains GitHub markdown.
     */
    private final String description;

}