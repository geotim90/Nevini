package de.nevini.api.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuStatus implements OsuEnum {

    LOVED(4, "Loved"),
    QUALIFIED(3, "Qualified"),
    APPROVED(2, "Approved"),
    RANKED(1, "Ranked"),
    PENDING(0, "Pending"),
    WIP(-1, "WIP"),
    GRAVEYARD(-2, "Graveyard");

    private final int id;
    private final String name;

}
