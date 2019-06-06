package de.nevini.api.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuTeam {

    NONE(0, "None"),
    BLUE(1, "Blue"),
    RED(2, "Red");

    private final int id;
    private final String name;

}
