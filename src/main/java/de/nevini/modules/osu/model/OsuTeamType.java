package de.nevini.modules.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuTeamType implements OsuEnum {

    HEAD_TO_HEAD(0, "Head to head"),
    TAG_CO_OP(1, "Tag Co-op"),
    TEAM_VS(2, "Team vs"),
    TAG_TEAM_VS(3, "Tag Team vs");

    private final int id;
    private final String name;

}
