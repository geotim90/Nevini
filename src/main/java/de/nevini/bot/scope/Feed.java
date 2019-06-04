package de.nevini.bot.scope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Feed {

    OSU_EVENTS("osu.events", "osu! events"),
    OSU_RECENT("osu.recent", "osu! plays");

    private final String type;
    private final String name;

}
