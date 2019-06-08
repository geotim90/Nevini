package de.nevini.bot.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Feed {

    GUILDS("guilds", "guild events", true),
    OSU_EVENTS("osu.events", "osu! events", false),
    OSU_RECENT("osu.recent", "osu! plays", false);

    private final String type;
    private final String name;
    private final boolean ownerOnly;

}
