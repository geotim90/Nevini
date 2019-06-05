package de.nevini.api.osu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuBeatmapApproved {

    LOVED(4, "Loved"),
    QUALIFIED(3, "Qualified"),
    APPROVED(2, "Approved"),
    RANKED(1, "Ranked"),
    PENDING(0, "Pending"),
    WIP(-1, "WIP"),
    GRAVEYARD(-2, "Graveyard");

    private final int approved;
    private final String name;

}
